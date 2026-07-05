package com.naadi.ojas.controller;

import com.naadi.ojas.dto.ContactInquiryResponse;
import com.naadi.ojas.service.ContactInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/contact-inquiries")
@RequiredArgsConstructor
public class AdminContactInquiryController {

    private final ContactInquiryService contactInquiryService;

    @GetMapping
    public List<ContactInquiryResponse> getAllInquiries() {
        return contactInquiryService.getAllInquiries();
    }

    @PatchMapping("/{id}/status")
    public ContactInquiryResponse updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return contactInquiryService.updateStatus(id, status);
    }
}