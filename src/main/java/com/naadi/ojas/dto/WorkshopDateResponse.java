package com.naadi.ojas.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class WorkshopDateResponse {

    private Long id;
    private LocalDate workshopDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer seats;
    private String mode;
    private String meetingLink;
}