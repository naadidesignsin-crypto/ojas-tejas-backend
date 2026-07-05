package com.naadi.ojas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "workshop_bookings")
public class WorkshopBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workshop_id")
    private Long workshopId;

    @Column(name = "workshop_date_id")
    private Long workshopDateId;

    @Column(name = "workshop_title", nullable = false, length = 200)
    private String workshopTitle;

    @Column(name = "selected_date_label", nullable = false, length = 200)
    private String selectedDateLabel;

    @Column(name = "parent_name", nullable = false, length = 100)
    private String parentName;

    @Column(name = "child_name", nullable = false, length = 100)
    private String childName;

    @Column(name = "child_age")
    private Integer childAge;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 700)
    private String message;

    @Column(name = "booking_key", length = 700)
    private String bookingKey;

    @Column(nullable = false, length = 30)
    private String status = "NEW";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void beforeSave() {
        createdAt = LocalDateTime.now();

        if (status == null || status.isBlank()) {
            status = "NEW";
        }
    }
}