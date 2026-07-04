package com.naadi.ojas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "activity_submissions")
public class ActivitySubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;

    private String activityTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageData;

    private Boolean approved = false;

    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;

    @PrePersist
    public void beforeSave() {
        createdAt = LocalDateTime.now();
    }
}