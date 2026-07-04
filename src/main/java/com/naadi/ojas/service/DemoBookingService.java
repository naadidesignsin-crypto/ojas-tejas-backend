package com.naadi.ojas.service;

import com.naadi.ojas.dto.DemoBookingRequest;
import com.naadi.ojas.dto.DemoBookingResponse;
import com.naadi.ojas.dto.SendDemoLinkRequest;
import com.naadi.ojas.entity.DemoBooking;
import com.naadi.ojas.repository.DemoBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoBookingService {

    private final DemoBookingRepository demoBookingRepository;
    private final EmailService emailService;

    public DemoBookingResponse createDemoBooking(DemoBookingRequest request) {
        DemoBooking demoBooking = mapToEntity(request);

        DemoBooking savedBooking = demoBookingRepository.save(demoBooking);

        return mapToResponse(savedBooking);
    }

    public List<DemoBookingResponse> getAllBookings() {
        return demoBookingRepository.findAll()
                .stream()
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

        return mapToResponse(demoBooking);
    }

    private DemoBooking findBookingById(Long id) {
        return demoBookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Demo booking not found with id: " + id
                ));
    }

    private DemoBooking mapToEntity(DemoBookingRequest request) {
        DemoBooking demoBooking = new DemoBooking();

        demoBooking.setParentName(request.getParentName());
        demoBooking.setChildName(request.getChildName());
        demoBooking.setChildAge(request.getChildAge());
        demoBooking.setPhone(request.getPhone());
        demoBooking.setEmail(request.getEmail());
        demoBooking.setPreferredClass(request.getPreferredClass());
        demoBooking.setMessage(request.getMessage());

        return demoBooking;
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

        return response;
    }
}