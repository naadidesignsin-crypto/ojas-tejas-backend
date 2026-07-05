package com.naadi.ojas.repository;

import com.naadi.ojas.entity.ContactInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {

    List<ContactInquiry> findAllByOrderByCreatedAtDesc();
}