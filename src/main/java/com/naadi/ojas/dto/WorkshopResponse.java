package com.naadi.ojas.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class WorkshopResponse {

    private Long id;
    private String title;
    private String description;
    private String ageGroup;
    private String level;
    private String priceLabel;
    private String imageData;
    private Boolean published;
    private LocalDateTime createdAt;
    private List<WorkshopDateResponse> dates;
}