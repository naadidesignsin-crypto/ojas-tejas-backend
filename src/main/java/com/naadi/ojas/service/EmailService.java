package com.naadi.ojas.service;

import com.naadi.ojas.entity.DemoBooking;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendDemoLiveLink(
            DemoBooking booking,
            String liveLink,
            String note
    ) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(booking.getEmail());
            message.setSubject("Your Ojas by Tejas Demo Class Live Link");
            message.setText(buildEmailBody(booking, liveLink, note));

            javaMailSender.send(message);

            System.out.println("Email sent successfully to: " + booking.getEmail());
        } catch (MailException exception) {
            System.out.println("Email sending failed for: " + booking.getEmail());
            exception.printStackTrace();
            throw new RuntimeException("Failed to send email. Please check Gmail SMTP configuration.");
        }
    }

    private String buildEmailBody(
            DemoBooking booking,
            String liveLink,
            String note
    ) {
        StringBuilder builder = new StringBuilder();

        builder.append("Hi ")
                .append(booking.getParentName())
                .append(",\n\n");

        builder.append("Thank you for booking a demo class with Ojas by Tejas.\n\n");

        builder.append("Child Name: ")
                .append(booking.getChildName())
                .append("\n");

        builder.append("Preferred Class: ")
                .append(booking.getPreferredClass())
                .append("\n\n");

        builder.append("Your live demo class link is:\n")
                .append(liveLink)
                .append("\n\n");

        if (note != null && !note.isBlank()) {
            builder.append("Note:\n")
                    .append(note)
                    .append("\n\n");
        }

        builder.append("Please join on time.\n\n");
        builder.append("Regards,\n");
        builder.append("Ojas by Tejas Team");

        return builder.toString();
    }
}