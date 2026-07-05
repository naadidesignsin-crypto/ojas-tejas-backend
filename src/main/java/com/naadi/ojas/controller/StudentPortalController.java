package com.naadi.ojas.controller;

import com.naadi.ojas.dto.StudentLoginRequest;
import com.naadi.ojas.dto.StudentLoginResponse;
import com.naadi.ojas.dto.WorkshopBookingResponse;
import com.naadi.ojas.service.StudentPortalService;
import com.naadi.ojas.service.WorkshopBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentPortalController {

    private final StudentPortalService studentPortalService;
    private final WorkshopBookingService workshopBookingService;

    @PostMapping("/login")
    public StudentLoginResponse login(
            @Valid @RequestBody StudentLoginRequest request
    ) {
        return studentPortalService.login(request);
    }

    @PostMapping("/workshop-bookings")
    public List<WorkshopBookingResponse> getWorkshopBookings(
            @Valid @RequestBody StudentLoginRequest request
    ) {
        return workshopBookingService.getStudentWorkshopBookings(
                request.getEmail(),
                request.getPhone()
        );
    }
}