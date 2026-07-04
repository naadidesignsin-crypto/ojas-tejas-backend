package com.naadi.ojas.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudentLoginResponse {

    private String parentName;
    private String childName;
    private String email;
    private String phone;
    private Integer registeredBookings;
    private Integer enabledLiveClasses;
    private List<StudentLiveClassResponse> liveClasses;
}