package com.yash.ytms.controller;

import com.yash.ytms.domain.IcsRequest;
import com.yash.ytms.util.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 28-02-2024
 */
@RestController
@RequestMapping("/ics")
public class IcsController {
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailUtil emailUtil;

    @PostMapping("/generate-ics")
    public ResponseEntity<byte[]> generateIcsFile(@RequestBody IcsRequest request) {
        String eventName = request.getEventName();
        String emailTo="sdnyanesh352@gmail.com";
        Date startDate = request.getStartTime();
        Date endDate = request.getEndTime();
        String organizerName = request.getOrganizerName();
        String organizerEmail = request.getOrganizerEmail();
        List<String> attendees = request.getAttendees();

        byte[] icsContent;
        try {
            icsContent = generateIcsContent(eventName, startDate, endDate, organizerName, organizerEmail, attendees);
            this.emailUtil.sendEmailWithAttachment(icsContent,emailTo);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=calendar-event.ics")
                .body(icsContent);
    }
    private byte[] generateIcsContent(String eventName, Date startDate, Date endDate, String organizerName, String organizerEmail, List<String> attendees) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            writer.write("BEGIN:VCALENDAR\r\n");
            writer.write("VERSION:2.0\r\n");
            writer.write("BEGIN:VEVENT\r\n");
            writer.write("SUMMARY:" + eventName + "\r\n");
            writer.write("DTSTART:" + dateFormat.format(startDate) + "Z\r\n");
            writer.write("DTEND:" + dateFormat.format(endDate) + "Z\r\n");

            // Set organizer dynamically
            writer.write("ORGANIZER;CN=" + organizerName + ":mailto:" + organizerEmail + "\r\n");

            // Add attendees dynamically
            for (String attendee : attendees) {
                writer.write("ATTENDEE;RSVP=TRUE;PARTSTAT=NEEDS-ACTION;ROLE=REQ-PARTICIPANT:mailto:" + attendee + "\r\n");
            }

            writer.write("END:VEVENT\r\n");
            writer.write("END:VCALENDAR\r\n");
        }
        return outputStream.toByteArray();
    }
}
