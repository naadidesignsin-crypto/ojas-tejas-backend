package com.naadi.ojas.controller;

import com.naadi.ojas.dto.WorkshopBookingRequest;
import com.naadi.ojas.dto.WorkshopBookingResponse;
import com.naadi.ojas.service.WorkshopBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workshop-bookings")
@RequiredArgsConstructor
public class WorkshopBookingController {

    private final WorkshopBookingService workshopBookingService;

    @PostMapping
    public WorkshopBookingResponse createBooking(
            @Valid @RequestBody WorkshopBookingRequest request
    ) {
        return workshopBookingService.createBooking(request);
    }
}