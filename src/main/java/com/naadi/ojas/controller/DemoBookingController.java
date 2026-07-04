package com.naadi.ojas.controller;

import com.naadi.ojas.dto.DemoBookingRequest;
import com.naadi.ojas.dto.DemoBookingResponse;
import com.naadi.ojas.service.DemoBookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo-bookings")
public class DemoBookingController {

    private final DemoBookingService demoBookingService;

    public DemoBookingController(DemoBookingService demoBookingService) {
        this.demoBookingService = demoBookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemoBookingResponse createDemoBooking(
            @Valid @RequestBody DemoBookingRequest request
    ) {
        return demoBookingService.createDemoBooking(request);
    }
}