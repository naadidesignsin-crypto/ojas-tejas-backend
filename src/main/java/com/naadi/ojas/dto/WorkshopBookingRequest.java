package com.naadi.ojas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkshopBookingRequest {

    @NotNull(message = "Workshop id is required")
    private Long workshopId;

    private Long workshopDateId;

    @NotBlank(message = "Workshop title is required")
    private String workshopTitle;

    @NotBlank(message = "Workshop date is required")
    private String selectedDateLabel;

    @NotBlank(message = "Parent name is required")
    private String parentName;

    @NotBlank(message = "Child name is required")
    private String childName;

    @Min(value = 3, message = "Age should be at least 3")
    @Max(value = 18, message = "Age should be below 18")
    private Integer childAge;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter valid email")
    private String email;

    private String message;
}