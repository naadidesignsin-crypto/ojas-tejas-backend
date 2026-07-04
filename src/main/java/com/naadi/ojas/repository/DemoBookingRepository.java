package com.naadi.ojas.repository;

import com.naadi.ojas.entity.DemoBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DemoBookingRepository extends JpaRepository<DemoBooking, Long> {
    Optional<DemoBooking> findByBookingKey(String bookingKey);

    List<DemoBooking> findAllByOrderByCreatedAtDesc();

    @Query("""
            SELECT d
            FROM DemoBooking d
            WHERE LOWER(TRIM(d.email)) = LOWER(TRIM(:email))
            AND TRIM(d.phone) = TRIM(:phone)
            ORDER BY d.createdAt DESC
            """)
    List<DemoBooking> findStudentBookings(
            @Param("email") String email,
            @Param("phone") String phone
    );
}
