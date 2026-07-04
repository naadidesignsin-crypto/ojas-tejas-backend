package com.naadi.ojas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkshopRequest {

    @NotBlank(message = "Workshop title is required")
    @Size(max = 140, message = "Workshop title must be under 140 characters")
    private String title;

    @NotBlank(message = "Workshop description is required")
    @Size(max = 3000, message = "Description must be under 3000 characters")
    private String description;

    @Size(max = 80, message = "Age group must be under 80 characters")
    private String ageGroup;

    @Size(max = 80, message = "Level must be under 80 characters")
    private String level;

    @Size(max = 80, message = "Price label must be under 80 characters")
    private String priceLabel;

    private String imageData;
}