package com.naadi.ojas.controller;

import com.naadi.ojas.dto.ContactInquiryRequest;
import com.naadi.ojas.dto.ContactInquiryResponse;
import com.naadi.ojas.service.ContactInquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact-inquiries")
@RequiredArgsConstructor
public class ContactInquiryController {

    private final ContactInquiryService contactInquiryService;

    @PostMapping
    public ContactInquiryResponse createInquiry(
            @Valid @RequestBody ContactInquiryRequest request
    ) {
        return contactInquiryService.createInquiry(request);
    }
}