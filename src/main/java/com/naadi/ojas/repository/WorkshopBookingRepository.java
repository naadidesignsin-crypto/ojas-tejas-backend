package com.naadi.ojas.repository;

import com.naadi.ojas.entity.WorkshopBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkshopBookingRepository extends JpaRepository<WorkshopBooking, Long> {

    Optional<WorkshopBooking> findByBookingKey(String bookingKey);

    List<WorkshopBooking> findAllByOrderByCreatedAtDesc();
}