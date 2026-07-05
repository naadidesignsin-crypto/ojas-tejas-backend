package com.naadi.ojas.service;

import com.naadi.ojas.dto.ContactInquiryRequest;
import com.naadi.ojas.dto.ContactInquiryResponse;
import com.naadi.ojas.entity.ContactInquiry;
import com.naadi.ojas.repository.ContactInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContactInquiryService {

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "NEW",
            "CONTACTED",
            "CLOSED"
    );

    private final ContactInquiryRepository contactInquiryRepository;
    private final EmailService emailService;

    public ContactInquiryResponse createInquiry(ContactInquiryRequest request) {
        ContactInquiry inquiry = new ContactInquiry();

        inquiry.setParentName(clean(request.getParentName()));
        inquiry.setEmail(clean(request.getEmail()));
        inquiry.setPhone(clean(request.getPhone()));
        inquiry.setMessage(clean(request.getMessage()));
        inquiry.setStatus("NEW");

        ContactInquiry savedInquiry = contactInquiryRepository.save(inquiry);

        emailService.sendContactInquiryAcknowledgement(savedInquiry);

        return mapToResponse(savedInquiry);
    }

    public List<ContactInquiryResponse> getAllInquiries() {
        return contactInquiryRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ContactInquiryResponse updateStatus(Long id, String status) {
        ContactInquiry inquiry = contactInquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Contact inquiry not found with id: " + id
                ));

        String newStatus = clean(status).toUpperCase();

        if (!ALLOWED_STATUSES.contains(newStatus)) {
            throw new RuntimeException("Invalid inquiry status: " + status);
        }

        inquiry.setStatus(newStatus);

        ContactInquiry savedInquiry = contactInquiryRepository.save(inquiry);

        return mapToResponse(savedInquiry);
    }

    private ContactInquiryResponse mapToResponse(ContactInquiry inquiry) {
        ContactInquiryResponse response = new ContactInquiryResponse();

        response.setId(inquiry.getId());
        response.setParentName(inquiry.getParentName());
        response.setEmail(inquiry.getEmail());
        response.setPhone(inquiry.getPhone());
        response.setMessage(inquiry.getMessage());
        response.setStatus(inquiry.getStatus());
        response.setCreatedAt(inquiry.getCreatedAt());

        return response;
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }
}