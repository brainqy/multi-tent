package com.yash.ytms.controller;

import com.yash.ytms.domain.IcsRequest;
import com.yash.ytms.dto.ResponseWrapperDto;
import com.yash.ytms.dto.ScheduleEventDto;
import com.yash.ytms.dto.YtmsUserDto;
import com.yash.ytms.services.IServices.IScheduleEventService;
import com.yash.ytms.services.IServices.IYtmsUserService;
import com.yash.ytms.util.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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
@RequestMapping("/interview")
public class IcsController {
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private IScheduleEventService scheduleEventService;
    @Autowired
    private IYtmsUserService userService;
    @Autowired
    ModelMapper mapper;
    @GetMapping("/getit")
    public String getItDone(){
        return "Get it working";
    }

    @PostMapping("/generate-ics")
    public ResponseEntity<ResponseWrapperDto> generateIcsFile(@RequestBody IcsRequest request, Principal principal) {
        ResponseWrapperDto responseWrapperDto= new ResponseWrapperDto();
        String email=principal.getName();
       YtmsUserDto userDto= userService.getUserByEmailAdd(email);
        String eventName = request.getTitle();
        List<String> emailTo = request.getAttendees();
        Date startDate = request.getStartTime();
        Date endDate = request.getEndTime();
        String organizerName = request.getOrganizerName();
        String organizerEmail = request.getOrganizerEmail();
        List<String> attendees = request.getAttendees();

        byte[] icsContent;
        try {
            icsContent = generateIcsContent(eventName, startDate, endDate, organizerName, organizerEmail, attendees);
            this.emailUtil.sendEmailWithAttachment(icsContent,emailTo);
            ScheduleEventDto scheduleEventDto = new ScheduleEventDto();
            scheduleEventDto.setTitle(request.getTitle());
            scheduleEventDto.setStart(request.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            scheduleEventDto.setEnd(request.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            scheduleEventDto.setColor(request.getPrimaryColor());
            scheduleEventDto.setScheduleUser(userDto);
            this.scheduleEventService.createScheduleEvent(scheduleEventDto, principal);
            responseWrapperDto.setStatus("SUCCESS");
            responseWrapperDto.setMessage("Interview is scheduled successfully!");
            responseWrapperDto.setData("icsContent");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(responseWrapperDto, HttpStatus.OK);
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
