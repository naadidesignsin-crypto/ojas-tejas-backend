package com.naadi.ojas.controller;

import com.naadi.ojas.dto.ActivitySubmissionRequest;
import com.naadi.ojas.dto.ActivitySubmissionResponse;
import com.naadi.ojas.service.ActivitySubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities/submissions")
@RequiredArgsConstructor
public class ActivitySubmissionController {

    private final ActivitySubmissionService activitySubmissionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivitySubmissionResponse submitActivity(
            @Valid @RequestBody ActivitySubmissionRequest request
    ) {
        return activitySubmissionService.submitActivity(request);
    }
}