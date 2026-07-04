package com.naadi.ojas.controller;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminDatabaseFixController {

    private final EntityManager entityManager;

    @DeleteMapping("/api/admin/db-fix/activity-submissions")
    @Transactional
    public Map<String, String> dropActivitySubmissionsTable() {
        entityManager
                .createNativeQuery("DROP TABLE IF EXISTS activity_submissions")
                .executeUpdate();

        return Map.of(
                "status", "success",
                "message", "activity_submissions table dropped. Restart backend to recreate it."
        );
    }
}