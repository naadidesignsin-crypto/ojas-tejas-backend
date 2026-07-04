package com.naadi.ojas.repository;

import com.naadi.ojas.entity.ActivitySubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitySubmissionRepository
        extends JpaRepository<ActivitySubmission, Long> {

    List<ActivitySubmission> findByApprovedFalseOrderByCreatedAtDesc();

    List<ActivitySubmission> findByApprovedTrueOrderByApprovedAtDesc();
}