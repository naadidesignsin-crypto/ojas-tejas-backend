package com.naadi.ojas.controller;

import com.naadi.ojas.dto.ActivitySubmissionResponse;
import com.naadi.ojas.service.ActivitySubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final ActivitySubmissionService activitySubmissionService;

    @GetMapping("/activities")
    public List<ActivitySubmissionResponse> getApprovedActivities() {
        return activitySubmissionService.getApprovedGalleryItems();
    }
}