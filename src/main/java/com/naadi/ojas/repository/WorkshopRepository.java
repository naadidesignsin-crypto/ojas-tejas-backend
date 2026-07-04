package com.naadi.ojas.repository;

import com.naadi.ojas.entity.Workshop;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    @EntityGraph(attributePaths = "dates")
    List<Workshop> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "dates")
    List<Workshop> findByPublishedTrueOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "dates")
    Optional<Workshop> findByIdAndPublishedTrue(Long id);
}