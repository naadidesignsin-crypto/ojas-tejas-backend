package com.naadi.ojas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceRequest {

    @NotNull(message = "Booking id is required")
    private Long bookingId;

    @NotNull(message = "Class date is required")
    private LocalDate classDate;

    @NotBlank(message = "Status is required")
    private String status;

    private String note;
}