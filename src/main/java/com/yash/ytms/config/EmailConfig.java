package com.yash.ytms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private String mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;


    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();

        javaMailSenderImpl.setHost(mailHost);
        javaMailSenderImpl.setPort(Integer.parseInt(mailPort));
        javaMailSenderImpl.setUsername(mailUsername);
        javaMailSenderImpl.setPassword(mailPassword);

        Properties props = javaMailSenderImpl.getJavaMailProperties();

        props.put("mail.smtp.starttls.enable", "true");
        return javaMailSenderImpl;

    }
}
