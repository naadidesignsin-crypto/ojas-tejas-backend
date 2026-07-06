package com.naadi.ojas.repository;

import com.naadi.ojas.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {

    Optional<AttendanceRecord> findByBookingIdAndClassDate(
            Long bookingId,
            LocalDate classDate
    );

    List<AttendanceRecord> findAllByOrderByClassDateDescCreatedAtDesc();

    @Query("""
            SELECT a
            FROM AttendanceRecord a
            WHERE LOWER(TRIM(a.email)) = LOWER(TRIM(:email))
            AND TRIM(a.phone) = TRIM(:phone)
            ORDER BY a.classDate DESC, a.createdAt DESC
            """)
    List<AttendanceRecord> findStudentAttendance(
            @Param("email") String email,
            @Param("phone") String phone
    );

    List<AttendanceRecord> findByBookingIdOrderByClassDateDesc(Long bookingId);
}