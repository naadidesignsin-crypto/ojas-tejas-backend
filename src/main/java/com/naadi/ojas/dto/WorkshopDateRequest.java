package com.naadi.ojas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class WorkshopDateRequest {

    @NotNull(message = "Workshop date is required")
    @FutureOrPresent(message = "Workshop date cannot be in the past")
    private LocalDate workshopDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Seats are required")
    @Min(value = 1, message = "Seats must be at least 1")
    private Integer seats;

    @Size(max = 60, message = "Mode must be under 60 characters")
    private String mode;

    @Size(max = 500, message = "Meeting link must be under 500 characters")
    private String meetingLink;
}