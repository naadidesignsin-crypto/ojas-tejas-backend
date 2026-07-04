package com.naadi.ojas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "demo_bookings")
@Data
public class DemoBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String parentName;

    @Column(nullable = false, length = 100)
    private String childName;

    @Column(nullable = false)
    private Integer childAge;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String preferredClass;

    @Column(length = 500)
    private String message;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "booking_key", unique = true, length = 500)
    private String bookingKey;

    @Column(name = "live_link", length = 500)
    private String liveLink;

    @Column(name = "live_link_note", length = 500)
    private String liveLinkNote;

    @Column(name = "live_link_sent")
    private Boolean liveLinkSent = false;

    @Column(name = "live_link_sent_at")
    private LocalDateTime liveLinkSentAt;

    @PrePersist
    private void beforeSave() {
        if (status == null || status.isBlank()) {
            status = "NEW";
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}
