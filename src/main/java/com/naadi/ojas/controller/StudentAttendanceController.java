package com.naadi.ojas.controller;

import com.naadi.ojas.dto.AttendanceSummaryResponse;
import com.naadi.ojas.dto.StudentAttendanceRequest;
import com.naadi.ojas.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/attendance")
@RequiredArgsConstructor
public class StudentAttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public AttendanceSummaryResponse getStudentAttendance(
            @Valid @RequestBody StudentAttendanceRequest request
    ) {
        return attendanceService.getStudentAttendanceSummary(
                request.getEmail(),
                request.getPhone()
        );
    }
}