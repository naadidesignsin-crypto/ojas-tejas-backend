package com.naadi.ojas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DemoBookingResponse {

    private Long id;
    private String parentName;
    private String childName;
    private Integer childAge;
    private String phone;
    private String email;
    private String preferredClass;
    private LocalDateTime createdAt;
    private String message;
    private String liveLink;
    private String liveLinkNote;
    private Boolean liveLinkSent;
    private LocalDateTime liveLinkSentAt;
}