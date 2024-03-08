package com.yash.ytms.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSetPasswordEmail(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("SetPassword");
        mimeMessageHelper.setText("""
                	        
                 <a href="http://localhost:4200/reset-password?session=%s">click here to reset password</a>
                	        
                """.formatted(Base64.getEncoder().encodeToString(email.getBytes())), true);

        javaMailSender.send(mimeMessage);
    }
    public void pendingReferralEmail(String referralEmail) throws MessagingException {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email= authentication.getName();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(referralEmail);
        mimeMessageHelper.setSubject("Activate your account");
        mimeMessageHelper.setText("""
                	        
                 <a href="http://localhost:4200/register?ref=%s">click here to join now</a>
                	        
                """.formatted(Base64.getEncoder().encodeToString(email.getBytes())), true);     javaMailSender.send(mimeMessage);
    }
    public void sendEmailWithAttachment(byte[] attachment, List<String> refEmail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        for (String emailAddress : refEmail) {
            helper.addTo(emailAddress);
        }
        helper.setSubject("Event Invitation");
        helper.setText("Please find the event attachment.");

        // Attach the .ics file
        helper.addAttachment("event.ics", new ByteArrayResource(attachment), "text/calendar");

        javaMailSender.send(message);
    }
}
