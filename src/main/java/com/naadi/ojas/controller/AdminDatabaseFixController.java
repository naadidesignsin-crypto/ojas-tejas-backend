package com.naadi.ojas.controller;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/db-fix")
public class AdminDatabaseFixController {

    private final EntityManager entityManager;

    @PostMapping("/activity-submissions")
    @Transactional
    public Map<String, String> createActivitySubmissionsTable() {
        entityManager.createNativeQuery("""
                CREATE TABLE IF NOT EXISTS activity_submissions (
                    id BIGSERIAL PRIMARY KEY,
                    student_name VARCHAR(100) NOT NULL,
                    activity_title VARCHAR(100) NOT NULL,
                    image_data TEXT NOT NULL,
                    approved BOOLEAN NOT NULL DEFAULT FALSE,
                    created_at TIMESTAMP,
                    approved_at TIMESTAMP
                )
                """).executeUpdate();

        return Map.of(
                "status", "success",
                "message", "activity_submissions table created"
        );
    }

    @DeleteMapping("/activity-submissions")
    @Transactional
    public Map<String, String> dropActivitySubmissionsTable() {
        entityManager
                .createNativeQuery("DROP TABLE IF EXISTS activity_submissions")
                .executeUpdate();

        return Map.of(
                "status", "success",
                "message", "activity_submissions table dropped"
        );
    }
}