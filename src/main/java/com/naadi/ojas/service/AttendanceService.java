package com.naadi.ojas.service;

import com.naadi.ojas.dto.AttendanceRequest;
import com.naadi.ojas.dto.AttendanceResponse;
import com.naadi.ojas.dto.AttendanceSummaryResponse;
import com.naadi.ojas.entity.AttendanceRecord;
import com.naadi.ojas.entity.DemoBooking;
import com.naadi.ojas.repository.AttendanceRepository;
import com.naadi.ojas.repository.DemoBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "PRESENT",
            "ABSENT",
            "LATE"
    );

    private final AttendanceRepository attendanceRepository;
    private final DemoBookingRepository demoBookingRepository;

    public AttendanceResponse saveAttendance(AttendanceRequest request) {
        String status = clean(request.getStatus()).toUpperCase();

        if (!ALLOWED_STATUSES.contains(status)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid attendance status: " + request.getStatus()
            );
        }

        DemoBooking booking = demoBookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Demo booking not found with id: " + request.getBookingId()
                ));

        AttendanceRecord attendanceRecord = attendanceRepository
                .findByBookingIdAndClassDate(
                        request.getBookingId(),
                        request.getClassDate()
                )
                .orElseGet(AttendanceRecord::new);

        attendanceRecord.setBookingId(booking.getId());
        attendanceRecord.setParentName(clean(booking.getParentName()));
        attendanceRecord.setChildName(clean(booking.getChildName()));
        attendanceRecord.setChildAge(booking.getChildAge());
        attendanceRecord.setPhone(clean(booking.getPhone()));
        attendanceRecord.setEmail(clean(booking.getEmail()));
        attendanceRecord.setClassTitle(clean(booking.getPreferredClass()));
        attendanceRecord.setClassDate(request.getClassDate());
        attendanceRecord.setStatus(status);
        attendanceRecord.setNote(cleanNullable(request.getNote()));

        AttendanceRecord savedRecord = attendanceRepository.save(attendanceRecord);

        return mapToResponse(savedRecord);
    }

    public List<AttendanceResponse> getAllAttendance() {
        return attendanceRepository.findAllByOrderByClassDateDescCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AttendanceResponse> getAttendanceByBookingId(Long bookingId) {
        return attendanceRepository.findByBookingIdOrderByClassDateDesc(bookingId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AttendanceSummaryResponse getStudentAttendanceSummary(
            String email,
            String phone
    ) {
        List<AttendanceResponse> records = attendanceRepository
                .findStudentAttendance(clean(email), clean(phone))
                .stream()
                .map(this::mapToResponse)
                .toList();

        int total = records.size();

        int present = (int) records.stream()
                .filter(record -> "PRESENT".equalsIgnoreCase(record.getStatus()))
                .count();

        int late = (int) records.stream()
                .filter(record -> "LATE".equalsIgnoreCase(record.getStatus()))
                .count();

        int absent = (int) records.stream()
                .filter(record -> "ABSENT".equalsIgnoreCase(record.getStatus()))
                .count();

        double percentage = total == 0
                ? 0.0
                : ((present + late) * 100.0) / total;

        return AttendanceSummaryResponse.builder()
                .totalClasses(total)
                .presentCount(present)
                .absentCount(absent)
                .lateCount(late)
                .attendancePercentage(roundTwoDecimals(percentage))
                .records(records)
                .build();
    }

    private AttendanceResponse mapToResponse(AttendanceRecord record) {
        AttendanceResponse response = new AttendanceResponse();

        response.setId(record.getId());
        response.setBookingId(record.getBookingId());
        response.setParentName(record.getParentName());
        response.setChildName(record.getChildName());
        response.setChildAge(record.getChildAge());
        response.setPhone(record.getPhone());
        response.setEmail(record.getEmail());
        response.setClassTitle(record.getClassTitle());
        response.setClassDate(record.getClassDate());
        response.setStatus(record.getStatus());
        response.setNote(record.getNote());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());

        return response;
    }

    private Double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
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
}