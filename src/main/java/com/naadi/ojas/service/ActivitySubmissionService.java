package com.naadi.ojas.service;

import com.naadi.ojas.dto.ActivitySubmissionRequest;
import com.naadi.ojas.dto.ActivitySubmissionResponse;
import com.naadi.ojas.entity.ActivitySubmission;
import com.naadi.ojas.repository.ActivitySubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivitySubmissionService {

    private final ActivitySubmissionRepository activitySubmissionRepository;

    public ActivitySubmissionResponse submitActivity(
            ActivitySubmissionRequest request
    ) {
        ActivitySubmission submission = new ActivitySubmission();

        submission.setStudentName(request.getStudentName());
        submission.setActivityTitle(request.getActivityTitle());
        submission.setImageData(request.getImageData());
        submission.setApproved(false);

        ActivitySubmission saved = activitySubmissionRepository.save(submission);

        return mapToResponse(saved);
    }

    public List<ActivitySubmissionResponse> getPendingSubmissions() {
        return activitySubmissionRepository.findByApprovedFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ActivitySubmissionResponse> getApprovedGalleryItems() {
        return activitySubmissionRepository.findByApprovedTrueOrderByApprovedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ActivitySubmissionResponse approveSubmission(Long id) {
        ActivitySubmission submission = activitySubmissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Activity submission not found with id: " + id
                ));

        submission.setApproved(true);
        submission.setApprovedAt(LocalDateTime.now());

        ActivitySubmission saved = activitySubmissionRepository.save(submission);

        return mapToResponse(saved);
    }

    private ActivitySubmissionResponse mapToResponse(
            ActivitySubmission submission
    ) {
        ActivitySubmissionResponse response = new ActivitySubmissionResponse();

        response.setId(submission.getId());
        response.setStudentName(submission.getStudentName());
        response.setActivityTitle(submission.getActivityTitle());
        response.setImageData(submission.getImageData());
        response.setApproved(submission.getApproved());
        response.setCreatedAt(submission.getCreatedAt());
        response.setApprovedAt(submission.getApprovedAt());

        return response;
    }
}