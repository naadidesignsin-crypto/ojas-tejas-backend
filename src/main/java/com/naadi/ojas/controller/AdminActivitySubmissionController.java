package com.naadi.ojas.controller;

import com.naadi.ojas.dto.ActivitySubmissionResponse;
import com.naadi.ojas.service.ActivitySubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/activity-submissions")
@RequiredArgsConstructor
public class AdminActivitySubmissionController {

    private final ActivitySubmissionService activitySubmissionService;

    @GetMapping
    public List<ActivitySubmissionResponse> getPendingSubmissions() {
        return activitySubmissionService.getPendingSubmissions();
    }

    @PatchMapping("/{id}/approve")
    public ActivitySubmissionResponse approveSubmission(
            @PathVariable Long id
    ) {
        return activitySubmissionService.approveSubmission(id);
    }
}