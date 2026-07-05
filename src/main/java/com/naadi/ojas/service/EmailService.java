package com.naadi.ojas.service;

import com.naadi.ojas.entity.DemoBooking;
import com.naadi.ojas.entity.WorkshopBooking;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendDemoBookingConfirmation(DemoBooking booking) {
        String subject = "Your free trial session request is received";

        String body = OjasEmailTemplate.buildEmail(
                "Free Trial Session Request Received",
                "Hi " + booking.getParentName() + ",",
                """
                <p>Thank you for registering your child for an Ojas by Tejas trial session.</p>

                <table style="width:100%%;margin-top:18px;border-collapse:collapse;">
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Student</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Age</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Class</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                </table>

                <p>Our team will share the live class link once the session is scheduled.</p>
                """.formatted(
                        booking.getChildName(),
                        booking.getChildAge(),
                        booking.getPreferredClass()
                ),
                null,
                null,
                "You can later login using your registered email and phone number to view enabled live classes."
        );

        sendHtmlEmail(booking.getEmail(), subject, body);
    }

    public void sendDemoLiveLink(
            DemoBooking booking,
            String liveLink,
            String note
    ) {
        String subject = "Your Ojas live class link is ready";

        String noteSection = "";

        if (note != null && !note.isBlank()) {
            noteSection = """
                    <p style="background:#fff3c9;padding:14px;border-radius:16px;">
                        <b>Teacher note:</b><br/>
                        %s
                    </p>
                    """.formatted(note.trim());
        }

        String body = OjasEmailTemplate.buildEmail(
                "Your Live Class Link Is Ready",
                "Hi " + booking.getParentName() + ",",
                """
                <p>The live class link for your child is now enabled.</p>

                <table style="width:100%%;margin-top:18px;border-collapse:collapse;">
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Student</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Class</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                </table>

                %s

                <p>Please join 5 minutes before the class starts.</p>
                """.formatted(
                        booking.getChildName(),
                        booking.getPreferredClass(),
                        noteSection
                ),
                "Join Live Class",
                liveLink,
                "Students can also login on the website using registered email and phone number to view the live class link."
        );

        sendHtmlEmail(booking.getEmail(), subject, body);
    }

    public void sendWorkshopBookingConfirmation(WorkshopBooking booking) {
        String subject = "Your workshop booking request is received";

        String body = OjasEmailTemplate.buildEmail(
                "Workshop Booking Request Received",
                "Hi " + booking.getParentName() + ",",
                """
                <p>Thank you for booking an Ojas by Tejas workshop.</p>

                <table style="width:100%%;margin-top:18px;border-collapse:collapse;">
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Workshop</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Date</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Student</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Status</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                </table>

                <p>Admin will review and confirm your workshop seat soon.</p>
                """.formatted(
                        booking.getWorkshopTitle(),
                        booking.getSelectedDateLabel(),
                        booking.getChildName(),
                        booking.getStatus()
                ),
                null,
                null,
                "You will be contacted if any extra details are needed for the workshop."
        );

        sendHtmlEmail(booking.getEmail(), subject, body);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    "UTF-8"
            );

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            javaMailSender.send(message);
        } catch (Exception exception) {
            throw new RuntimeException("Unable to send email", exception);
        }
    }

    public void sendWorkshopStatusUpdate(WorkshopBooking booking) {
        String subject = "Your Ojas workshop booking status is updated";

        String statusMessage = switch (booking.getStatus()) {
            case "CONFIRMED" -> """
                <p style="background:#e9fff2;padding:14px;border-radius:16px;color:#087536;">
                    <b>Good news!</b><br/>
                    Your child&apos;s workshop seat is confirmed.
                </p>
                """;

            case "CANCELLED" -> """
                <p style="background:#fff0f0;padding:14px;border-radius:16px;color:#b42318;">
                    <b>Workshop booking cancelled.</b><br/>
                    Please contact Ojas by Tejas if you need help or want to book another slot.
                </p>
                """;

            case "COMPLETED" -> """
                <p style="background:#eef4ff;padding:14px;border-radius:16px;color:#004aad;">
                    <b>Workshop completed.</b><br/>
                    Thank you for joining the Ojas by Tejas workshop.
                </p>
                """;

            default -> """
                <p style="background:#fff3c9;padding:14px;border-radius:16px;color:#9a6500;">
                    <b>Your workshop booking is received.</b><br/>
                    Admin will confirm the booking soon.
                </p>
                """;
        };

        String body = OjasEmailTemplate.buildEmail(
                "Workshop Booking Status Updated",
                "Hi " + booking.getParentName() + ",",
                """
                <p>Your Ojas by Tejas workshop booking status has been updated.</p>
    
                %s
    
                <table style="width:100%%;margin-top:18px;border-collapse:collapse;">
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Workshop</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Date</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Student</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>Status</b></td>
                        <td style="padding:10px;border-bottom:1px solid #d9e8ff;"><b>%s</b></td>
                    </tr>
                </table>
                """.formatted(
                        statusMessage,
                        booking.getWorkshopTitle(),
                        booking.getSelectedDateLabel(),
                        booking.getChildName(),
                        booking.getStatus()
                ),
                null,
                null,
                "For any questions, please contact Ojas by Tejas support."
        );

        sendHtmlEmail(booking.getEmail(), subject, body);
    }
}