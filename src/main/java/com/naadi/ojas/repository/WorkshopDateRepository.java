package com.naadi.ojas.repository;

import com.naadi.ojas.entity.WorkshopDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkshopDateRepository extends JpaRepository<WorkshopDate, Long> {

    List<WorkshopDate> findByWorkshopIdOrderByWorkshopDateAscStartTimeAsc(Long workshopId);
}