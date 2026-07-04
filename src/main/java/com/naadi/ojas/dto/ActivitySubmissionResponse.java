package com.naadi.ojas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActivitySubmissionResponse {

    private Long id;
    private String studentName;
    private String activityTitle;
    private String imageData;
    private Boolean approved;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
}