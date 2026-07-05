package com.naadi.ojas.repository;

import com.naadi.ojas.entity.WorkshopBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkshopBookingRepository extends JpaRepository<WorkshopBooking, Long> {

    Optional<WorkshopBooking> findByBookingKey(String bookingKey);

    List<WorkshopBooking> findAllByOrderByCreatedAtDesc();

    @Query("""
            SELECT w
            FROM WorkshopBooking w
            WHERE LOWER(TRIM(w.email)) = LOWER(TRIM(:email))
            AND TRIM(w.phone) = TRIM(:phone)
            ORDER BY w.createdAt DESC
            """)
    List<WorkshopBooking> findStudentWorkshopBookings(
            @Param("email") String email,
            @Param("phone") String phone
    );
}