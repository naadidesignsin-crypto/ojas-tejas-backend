package com.naadi.ojas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContactInquiryResponse {

    private Long id;
    private String parentName;
    private String email;
    private String phone;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}