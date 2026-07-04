package com.naadi.ojas.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudentLiveClassResponse {

    private Long bookingId;
    private String parentName;
    private String childName;
    private Integer childAge;
    private String preferredClass;
    private String liveLink;
    private String note;
    private LocalDateTime sentAt;
}