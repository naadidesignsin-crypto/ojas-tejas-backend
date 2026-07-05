package com.naadi.ojas.service;

import com.naadi.ojas.dto.WorkshopBookingRequest;
import com.naadi.ojas.dto.WorkshopBookingResponse;
import com.naadi.ojas.entity.WorkshopBooking;
import com.naadi.ojas.repository.WorkshopBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopBookingService {

    private final WorkshopBookingRepository workshopBookingRepository;

    public WorkshopBookingResponse createBooking(WorkshopBookingRequest request) {
        String bookingKey = buildBookingKey(request);

        WorkshopBooking booking = workshopBookingRepository
                .findByBookingKey(bookingKey)
                .orElseGet(WorkshopBooking::new);

        booking.setWorkshopId(request.getWorkshopId());
        booking.setWorkshopDateId(request.getWorkshopDateId());
        booking.setWorkshopTitle(clean(request.getWorkshopTitle()));
        booking.setSelectedDateLabel(clean(request.getSelectedDateLabel()));
        booking.setParentName(clean(request.getParentName()));
        booking.setChildName(clean(request.getChildName()));
        booking.setChildAge(request.getChildAge());
        booking.setPhone(clean(request.getPhone()));
        booking.setEmail(clean(request.getEmail()));
        booking.setMessage(cleanNullable(request.getMessage()));
        booking.setBookingKey(bookingKey);

        if (booking.getStatus() == null || booking.getStatus().isBlank()) {
            booking.setStatus("NEW");
        }

        WorkshopBooking savedBooking = workshopBookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }

    public List<WorkshopBookingResponse> getAllBookings() {
        return workshopBookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public WorkshopBookingResponse updateStatus(Long id, String status) {
        WorkshopBooking booking = workshopBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Workshop booking not found with id: " + id
                ));

        booking.setStatus(clean(status).toUpperCase());

        WorkshopBooking savedBooking = workshopBookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }

    private WorkshopBookingResponse mapToResponse(WorkshopBooking booking) {
        WorkshopBookingResponse response = new WorkshopBookingResponse();

        response.setId(booking.getId());
        response.setWorkshopId(booking.getWorkshopId());
        response.setWorkshopDateId(booking.getWorkshopDateId());
        response.setWorkshopTitle(booking.getWorkshopTitle());
        response.setSelectedDateLabel(booking.getSelectedDateLabel());
        response.setParentName(booking.getParentName());
        response.setChildName(booking.getChildName());
        response.setChildAge(booking.getChildAge());
        response.setPhone(booking.getPhone());
        response.setEmail(booking.getEmail());
        response.setMessage(booking.getMessage());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());

        return response;
    }

    private String buildBookingKey(WorkshopBookingRequest request) {
        return cleanKey(String.valueOf(request.getWorkshopId())) + "|" +
                cleanKey(String.valueOf(request.getWorkshopDateId())) + "|" +
                cleanKey(request.getEmail()) + "|" +
                cleanKey(request.getPhone()) + "|" +
                cleanKey(request.getChildName());
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private String cleanNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
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