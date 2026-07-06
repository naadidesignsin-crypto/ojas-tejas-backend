package com.naadi.ojas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "attendance_records",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_attendance_booking_date",
                        columnNames = {"booking_id", "class_date"}
                )
        }
)
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "parent_name", nullable = false, length = 120)
    private String parentName;

    @Column(name = "child_name", nullable = false, length = 120)
    private String childName;

    @Column(name = "child_age")
    private Integer childAge;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "class_title", nullable = false, length = 180)
    private String classTitle;

    @Column(name = "class_date", nullable = false)
    private LocalDate classDate;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 700)
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforeCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null || status.isBlank()) {
            status = "PRESENT";
        }
    }

    @PreUpdate
    public void beforeUpdate() {
        updatedAt = LocalDateTime.now();
    }
}