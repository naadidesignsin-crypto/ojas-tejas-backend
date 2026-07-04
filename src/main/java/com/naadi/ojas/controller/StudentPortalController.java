package com.naadi.ojas.controller;

import com.naadi.ojas.dto.StudentLoginRequest;
import com.naadi.ojas.dto.StudentLoginResponse;
import com.naadi.ojas.service.StudentPortalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentPortalController {

    private final StudentPortalService studentPortalService;

    @PostMapping("/login")
    public StudentLoginResponse login(
            @Valid @RequestBody StudentLoginRequest request
    ) {
        return studentPortalService.login(request);
    }
}