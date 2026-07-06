package com.naadi.ojas.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AttendanceSummaryResponse {

    private Integer totalClasses;
    private Integer presentCount;
    private Integer absentCount;
    private Integer lateCount;
    private Double attendancePercentage;
    private List<AttendanceResponse> records;
}