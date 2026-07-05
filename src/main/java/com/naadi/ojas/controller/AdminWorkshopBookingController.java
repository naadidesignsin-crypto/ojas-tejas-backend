package com.naadi.ojas.controller;

import com.naadi.ojas.dto.WorkshopBookingResponse;
import com.naadi.ojas.service.WorkshopBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/workshop-bookings")
@RequiredArgsConstructor
public class AdminWorkshopBookingController {

    private final WorkshopBookingService workshopBookingService;

    @GetMapping
    public List<WorkshopBookingResponse> getAllBookings() {
        return workshopBookingService.getAllBookings();
    }

    @PatchMapping("/{id}/status")
    public WorkshopBookingResponse updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return workshopBookingService.updateStatus(id, status);
    }
}