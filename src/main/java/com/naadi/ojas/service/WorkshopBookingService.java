package com.naadi.ojas.service;

import com.naadi.ojas.dto.WorkshopBookingRequest;
import com.naadi.ojas.dto.WorkshopBookingResponse;
import com.naadi.ojas.entity.WorkshopBooking;
import com.naadi.ojas.entity.WorkshopDate;
import com.naadi.ojas.repository.WorkshopBookingRepository;
import com.naadi.ojas.repository.WorkshopDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WorkshopBookingService {

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "NEW",
            "CONFIRMED",
            "CANCELLED",
            "COMPLETED"
    );

    private final WorkshopBookingRepository workshopBookingRepository;
    private final WorkshopDateRepository workshopDateRepository;
    private final EmailService emailService;

    @Transactional
    public WorkshopBookingResponse createBooking(WorkshopBookingRequest request) {
        String bookingKey = buildBookingKey(request);

        WorkshopBooking existingBooking = workshopBookingRepository
                .findByBookingKey(bookingKey)
                .orElse(null);

        if (existingBooking != null) {
            Integer remainingSeats = getRemainingSeats(
                    existingBooking.getWorkshopDateId()
            );

            return mapToResponse(existingBooking, true, remainingSeats);
        }

        Integer remainingSeats = decreaseSeat(request.getWorkshopDateId());

        WorkshopBooking booking = new WorkshopBooking();

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
        booking.setStatus("NEW");

        WorkshopBooking savedBooking = workshopBookingRepository.save(booking);

        emailService.sendWorkshopBookingConfirmation(savedBooking);

        return mapToResponse(savedBooking, false, remainingSeats);
    }

    public List<WorkshopBookingResponse> getAllBookings() {
        return workshopBookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(booking -> mapToResponse(
                        booking,
                        false,
                        getRemainingSeats(booking.getWorkshopDateId())
                ))
                .toList();
    }

    @Transactional
    public WorkshopBookingResponse updateStatus(Long id, String status) {
        WorkshopBooking booking = workshopBookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Workshop booking not found with id: " + id
                ));

        String newStatus = clean(status).toUpperCase();

        if (!ALLOWED_STATUSES.contains(newStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid workshop booking status: " + status
            );
        }

        String oldStatus = booking.getStatus();

        boolean statusChanged = oldStatus == null ||
                !oldStatus.equalsIgnoreCase(newStatus);

        if (!statusChanged) {
            return mapToResponse(
                    booking,
                    false,
                    getRemainingSeats(booking.getWorkshopDateId())
            );
        }

        boolean oldCancelled = "CANCELLED".equalsIgnoreCase(oldStatus);
        boolean newCancelled = "CANCELLED".equalsIgnoreCase(newStatus);

        Integer remainingSeats = getRemainingSeats(booking.getWorkshopDateId());

        if (!oldCancelled && newCancelled) {
            remainingSeats = increaseSeat(booking.getWorkshopDateId());
        }

        if (oldCancelled && !newCancelled) {
            remainingSeats = decreaseSeat(booking.getWorkshopDateId());
        }

        booking.setStatus(newStatus);

        WorkshopBooking savedBooking = workshopBookingRepository.save(booking);

        emailService.sendWorkshopStatusUpdate(savedBooking);

        return mapToResponse(savedBooking, false, remainingSeats);
    }

    private Integer decreaseSeat(Long workshopDateId) {
        if (workshopDateId == null) {
            return null;
        }

        WorkshopDate workshopDate = workshopDateRepository.findByIdForUpdate(
                workshopDateId
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Workshop date not found"
        ));

        int availableSeats = safeNumber(workshopDate.getAvailableSeats());

        if (availableSeats <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This workshop date is full. Please select another date."
            );
        }

        workshopDate.setAvailableSeats(availableSeats - 1);

        return workshopDate.getAvailableSeats();
    }

    private Integer increaseSeat(Long workshopDateId) {
        if (workshopDateId == null) {
            return null;
        }

        WorkshopDate workshopDate = workshopDateRepository.findByIdForUpdate(
                workshopDateId
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Workshop date not found"
        ));

        int availableSeats = safeNumber(workshopDate.getAvailableSeats());

        workshopDate.setAvailableSeats(availableSeats + 1);

        return workshopDate.getAvailableSeats();
    }

    private Integer getRemainingSeats(Long workshopDateId) {
        if (workshopDateId == null) {
            return null;
        }

        return workshopDateRepository.findById(workshopDateId)
                .map(WorkshopDate::getAvailableSeats)
                .orElse(null);
    }

    private WorkshopBookingResponse mapToResponse(
            WorkshopBooking booking,
            boolean alreadyBooked,
            Integer remainingSeats
    ) {
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
        response.setAlreadyBooked(alreadyBooked);
        response.setRemainingSeats(remainingSeats);

        return response;
    }

    private String buildBookingKey(WorkshopBookingRequest request) {
        return cleanKey(String.valueOf(request.getWorkshopId())) + "|" +
                cleanKey(String.valueOf(request.getWorkshopDateId())) + "|" +
                cleanKey(request.getEmail()) + "|" +
                cleanKey(request.getPhone()) + "|" +
                cleanKey(request.getChildName());
    }

    private int safeNumber(Integer value) {
        if (value == null) {
            return 0;
        }

        return value;
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