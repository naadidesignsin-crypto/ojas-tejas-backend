package com.naadi.ojas.repository;

import com.naadi.ojas.entity.WorkshopDate;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface WorkshopDateRepository extends JpaRepository<WorkshopDate, Long> {

    List<WorkshopDate> findByWorkshopIdOrderByWorkshopDateAscStartTimeAsc(Long workshopId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT d
        FROM WorkshopDate d
        WHERE d.id = :id
        """)
    Optional<WorkshopDate> findByIdForUpdate(@Param("id") Long id);
}