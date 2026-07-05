package com.naadi.ojas.service;

import com.naadi.ojas.dto.DemoBookingRequest;
import com.naadi.ojas.dto.DemoBookingResponse;
import com.naadi.ojas.dto.SendDemoLinkRequest;
import com.naadi.ojas.entity.DemoBooking;
import com.naadi.ojas.repository.DemoBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoBookingService {

    private final DemoBookingRepository demoBookingRepository;
    private final EmailService emailService;

    public DemoBookingResponse createDemoBooking(DemoBookingRequest request) {
        String bookingKey = buildBookingKey(request);

        DemoBooking demoBooking = demoBookingRepository
                .findByBookingKey(bookingKey)
                .orElseGet(DemoBooking::new);

        boolean newBooking = demoBooking.getId() == null;

        demoBooking.setParentName(request.getParentName().trim());
        demoBooking.setChildName(request.getChildName().trim());
        demoBooking.setChildAge(request.getChildAge());
        demoBooking.setPhone(request.getPhone().trim());
        demoBooking.setEmail(request.getEmail().trim());
        demoBooking.setPreferredClass(request.getPreferredClass().trim());
        demoBooking.setMessage(cleanValue(request.getMessage()));
        demoBooking.setBookingKey(bookingKey);

        if (demoBooking.getCreatedAt() == null) {
            demoBooking.setCreatedAt(LocalDateTime.now());
        }

        if (demoBooking.getLiveLinkSent() == null) {
            demoBooking.setLiveLinkSent(false);
        }

        DemoBooking savedBooking = demoBookingRepository.save(demoBooking);

        if (newBooking) {
            emailService.sendDemoBookingConfirmation(savedBooking);
        }

        return mapToResponse(savedBooking);
    }

    public List<DemoBookingResponse> getAllBookings() {
        return demoBookingRepository.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                DemoBooking::getCreatedAt,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                )
                .map(this::mapToResponse)
                .toList();
    }

    public DemoBookingResponse getBookingById(Long id) {
        DemoBooking demoBooking = findBookingById(id);

        return mapToResponse(demoBooking);
    }

    public DemoBookingResponse sendLiveLinkToBooking(
            Long id,
            SendDemoLinkRequest request
    ) {
        DemoBooking demoBooking = findBookingById(id);

        emailService.sendDemoLiveLink(
                demoBooking,
                request.getLiveLink(),
                request.getNote()
        );

        demoBooking.setLiveLink(request.getLiveLink().trim());
        demoBooking.setLiveLinkNote(cleanValue(request.getNote()));
        demoBooking.setLiveLinkSent(true);
        demoBooking.setLiveLinkSentAt(LocalDateTime.now());

        DemoBooking savedBooking = demoBookingRepository.save(demoBooking);

        return mapToResponse(savedBooking);
    }

    private DemoBooking findBookingById(Long id) {
        return demoBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Demo booking not found with id: " + id
                ));
    }

    private DemoBookingResponse mapToResponse(DemoBooking demoBooking) {
        DemoBookingResponse response = new DemoBookingResponse();

        response.setId(demoBooking.getId());
        response.setParentName(demoBooking.getParentName());
        response.setChildName(demoBooking.getChildName());
        response.setChildAge(demoBooking.getChildAge());
        response.setPhone(demoBooking.getPhone());
        response.setEmail(demoBooking.getEmail());
        response.setPreferredClass(demoBooking.getPreferredClass());
        response.setMessage(demoBooking.getMessage());
        response.setCreatedAt(demoBooking.getCreatedAt());

        response.setLiveLink(demoBooking.getLiveLink());
        response.setLiveLinkNote(demoBooking.getLiveLinkNote());
        response.setLiveLinkSent(
                demoBooking.getLiveLinkSent() != null
                        ? demoBooking.getLiveLinkSent()
                        : false
        );
        response.setLiveLinkSentAt(demoBooking.getLiveLinkSentAt());

        return response;
    }

    private String buildBookingKey(DemoBookingRequest request) {
        return cleanKey(request.getParentName()) + "|" +
                cleanKey(request.getChildName()) + "|" +
                cleanKey(request.getPhone()) + "|" +
                cleanKey(request.getEmail()) + "|" +
                cleanKey(request.getPreferredClass());
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

    private String cleanValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}