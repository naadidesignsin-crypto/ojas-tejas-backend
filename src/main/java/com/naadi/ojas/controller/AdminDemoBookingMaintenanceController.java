package com.naadi.ojas.controller;

import com.naadi.ojas.entity.DemoBooking;
import com.naadi.ojas.repository.DemoBookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/maintenance/demo-bookings")
public class AdminDemoBookingMaintenanceController {

    private final DemoBookingRepository demoBookingRepository;

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        List<DemoBooking> bookings = demoBookingRepository.findAll();

        Map<String, Integer> keyCounts = new HashMap<>();

        for (DemoBooking booking : bookings) {
            String key = buildBookingKey(booking);
            keyCounts.put(key, keyCounts.getOrDefault(key, 0) + 1);
        }

        long duplicateGroups = keyCounts.values()
                .stream()
                .filter(count -> count > 1)
                .count();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("totalBookings", bookings.size());
        response.put("duplicateGroups", duplicateGroups);
        response.put("message", "Demo booking summary loaded");

        return response;
    }

    @DeleteMapping("/{bookingId}")
    public Map<String, Object> deleteBookingById(
            @PathVariable Long bookingId
    ) {
        if (!demoBookingRepository.existsById(bookingId)) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "not_found");
            response.put("message", "Booking not found with id: " + bookingId);
            return response;
        }

        demoBookingRepository.deleteById(bookingId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("deletedBookingId", bookingId);
        response.put("message", "Demo booking deleted successfully");

        return response;
    }

    @PostMapping("/remove-duplicates")
    @Transactional
    public Map<String, Object> removeDuplicateBookings() {
        List<DemoBooking> bookings = demoBookingRepository.findAll();

        bookings.sort(Comparator.comparing(DemoBooking::getId));

        Map<String, DemoBooking> firstBookingByKey = new LinkedHashMap<>();
        List<DemoBooking> duplicatesToDelete = new ArrayList<>();
        List<Long> deletedIds = new ArrayList<>();

        for (DemoBooking booking : bookings) {
            String key = buildBookingKey(booking);

            if (!firstBookingByKey.containsKey(key)) {
                firstBookingByKey.put(key, booking);
            } else {
                duplicatesToDelete.add(booking);
                deletedIds.add(booking.getId());
            }
        }

        demoBookingRepository.deleteAll(duplicatesToDelete);

        List<DemoBooking> uniqueBookings = new ArrayList<>();

        for (DemoBooking booking : firstBookingByKey.values()) {
            booking.setBookingKey(buildBookingKey(booking));

            if (booking.getCreatedAt() == null) {
                booking.setCreatedAt(LocalDateTime.now());
            }

            if (booking.getLiveLinkSent() == null) {
                booking.setLiveLinkSent(false);
            }

            uniqueBookings.add(booking);
        }

        demoBookingRepository.saveAll(uniqueBookings);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("totalBeforeCleanup", bookings.size());
        response.put("duplicatesDeleted", duplicatesToDelete.size());
        response.put("deletedIds", deletedIds);
        response.put("remainingBookings", uniqueBookings.size());
        response.put("message", "Duplicate demo bookings removed and booking keys rebuilt");

        return response;
    }

    @PostMapping("/rebuild-booking-keys")
    @Transactional
    public Map<String, Object> rebuildBookingKeys() {
        List<DemoBooking> bookings = demoBookingRepository.findAll();

        for (DemoBooking booking : bookings) {
            booking.setBookingKey(buildBookingKey(booking));

            if (booking.getCreatedAt() == null) {
                booking.setCreatedAt(LocalDateTime.now());
            }

            if (booking.getLiveLinkSent() == null) {
                booking.setLiveLinkSent(false);
            }
        }

        demoBookingRepository.saveAll(bookings);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("updatedBookings", bookings.size());
        response.put("message", "Booking keys rebuilt successfully");

        return response;
    }

    @DeleteMapping("/all")
    public Map<String, Object> deleteAllBookings(
            @RequestParam String confirm
    ) {
        if (!"DELETE_ALL_DEMO_BOOKINGS".equals(confirm)) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "blocked");
            response.put("message", "Invalid confirmation text");
            response.put("requiredConfirm", "DELETE_ALL_DEMO_BOOKINGS");
            return response;
        }

        long count = demoBookingRepository.count();

        demoBookingRepository.deleteAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("deletedCount", count);
        response.put("message", "All demo bookings deleted successfully");

        return response;
    }

    private String buildBookingKey(DemoBooking booking) {
        return cleanKey(booking.getParentName()) + "|" +
                cleanKey(booking.getChildName()) + "|" +
                cleanKey(String.valueOf(booking.getChildAge())) + "|" +
                cleanKey(booking.getPhone()) + "|" +
                cleanKey(booking.getEmail()) + "|" +
                cleanKey(booking.getPreferredClass());
    }

    private String cleanKey(String value) {
        if (value == null) {
            return "";
        }

        return value
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", " ");
    }
}