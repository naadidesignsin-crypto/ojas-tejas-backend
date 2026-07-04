package com.naadi.ojas.dto;

import lombok.Getter;
import lombok.Setter;

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
    private String message;
}