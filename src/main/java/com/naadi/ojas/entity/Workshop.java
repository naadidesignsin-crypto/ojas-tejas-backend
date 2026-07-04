package com.naadi.ojas.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "workshops")
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "age_group", length = 80)
    private String ageGroup;

    @Column(length = 80)
    private String level;

    @Column(name = "price_label", length = 80)
    private String priceLabel;

    @Column(name = "image_data", columnDefinition = "TEXT")
    private String imageData;

    @Column(nullable = false)
    private Boolean published = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("workshopDate ASC, startTime ASC")
    private List<WorkshopDate> dates = new ArrayList<>();

    @PrePersist
    public void beforeSave() {
        createdAt = LocalDateTime.now();

        if (published == null) {
            published = false;
        }
    }
}