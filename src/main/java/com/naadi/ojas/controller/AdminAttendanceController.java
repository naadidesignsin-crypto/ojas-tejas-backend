package com.naadi.ojas.controller;

import com.naadi.ojas.dto.AttendanceRequest;
import com.naadi.ojas.dto.AttendanceResponse;
import com.naadi.ojas.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/attendance")
@RequiredArgsConstructor
public class AdminAttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public List<AttendanceResponse> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @GetMapping("/booking/{bookingId}")
    public List<AttendanceResponse> getAttendanceByBookingId(
            @PathVariable Long bookingId
    ) {
        return attendanceService.getAttendanceByBookingId(bookingId);
    }

    @PostMapping
    public AttendanceResponse saveAttendance(
            @Valid @RequestBody AttendanceRequest request
    ) {
        return attendanceService.saveAttendance(request);
    }
}