package com.naadi.ojas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceResponse {

    private Long id;
    private Long bookingId;
    private String parentName;
    private String childName;
    private Integer childAge;
    private String phone;
    private String email;
    private String classTitle;
    private LocalDate classDate;
    private String status;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}