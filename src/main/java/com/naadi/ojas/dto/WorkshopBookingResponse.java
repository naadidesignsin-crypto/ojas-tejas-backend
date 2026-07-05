package com.naadi.ojas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkshopBookingResponse {

    private Long id;
    private Long workshopId;
    private Long workshopDateId;
    private String workshopTitle;
    private String selectedDateLabel;
    private String parentName;
    private String childName;
    private Integer childAge;
    private String phone;
    private String email;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}