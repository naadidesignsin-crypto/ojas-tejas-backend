package com.naadi.ojas.service;

import com.naadi.ojas.dto.StudentLiveClassResponse;
import com.naadi.ojas.dto.StudentLoginRequest;
import com.naadi.ojas.dto.StudentLoginResponse;
import com.naadi.ojas.entity.DemoBooking;
import com.naadi.ojas.repository.DemoBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentPortalService {

    private final DemoBookingRepository demoBookingRepository;

    public StudentLoginResponse login(StudentLoginRequest request) {
        String email = clean(request.getEmail());
        String phone = clean(request.getPhone());

        List<DemoBooking> bookings = demoBookingRepository.findStudentBookings(
                email,
                phone
        );

        if (bookings.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "No registration found for this email and phone"
            );
        }

        DemoBooking primaryBooking = bookings.getFirst();

        List<StudentLiveClassResponse> liveClasses = bookings
                .stream()
                .filter(this::isLiveClassEnabled)
                .sorted(
                        Comparator.comparing(
                                DemoBooking::getLiveLinkSentAt,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                )
                .map(this::toLiveClassResponse)
                .toList();

        return StudentLoginResponse.builder()
                .parentName(primaryBooking.getParentName())
                .childName(primaryBooking.getChildName())
                .email(primaryBooking.getEmail())
                .phone(primaryBooking.getPhone())
                .registeredBookings(bookings.size())
                .enabledLiveClasses(liveClasses.size())
                .liveClasses(liveClasses)
                .build();
    }

    private boolean isLiveClassEnabled(DemoBooking booking) {
        return Boolean.TRUE.equals(booking.getLiveLinkSent())
                && booking.getLiveLink() != null
                && !booking.getLiveLink().isBlank();
    }

    private StudentLiveClassResponse toLiveClassResponse(DemoBooking booking) {
        return StudentLiveClassResponse.builder()
                .bookingId(booking.getId())
                .parentName(booking.getParentName())
                .childName(booking.getChildName())
                .childAge(booking.getChildAge())
                .preferredClass(booking.getPreferredClass())
                .liveLink(booking.getLiveLink())
                .note(booking.getLiveLinkNote())
                .sentAt(booking.getLiveLinkSentAt())
                .build();
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }
}