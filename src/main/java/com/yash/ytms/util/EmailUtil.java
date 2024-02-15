package com.yash.ytms.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Base64;

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
}
