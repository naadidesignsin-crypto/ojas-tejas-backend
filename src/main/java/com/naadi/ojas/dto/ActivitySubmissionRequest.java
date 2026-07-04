package com.naadi.ojas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivitySubmissionRequest {

    @NotBlank(message = "Student name is required")
    @Size(max = 100, message = "Student name must be under 100 characters")
    private String studentName;

    @NotBlank(message = "Activity title is required")
    @Size(max = 100, message = "Activity title must be under 100 characters")
    private String activityTitle;

    @NotBlank(message = "Drawing image is required")
    private String imageData;
}