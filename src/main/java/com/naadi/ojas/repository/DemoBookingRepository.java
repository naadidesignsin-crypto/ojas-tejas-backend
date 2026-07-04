package com.naadi.ojas.repository;

import com.naadi.ojas.entity.DemoBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DemoBookingRepository extends JpaRepository<DemoBooking, Long> {
    Optional<DemoBooking> findByBookingKey(String bookingKey);

    List<DemoBooking> findAllByOrderByCreatedAtDesc();
}
