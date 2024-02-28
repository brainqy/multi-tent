package com.yash.ytms.services.IServices;

import com.yash.ytms.dto.ReferralDto;
import com.yash.ytms.dto.YtmsUserDto;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project ytms-api
 * @since 14-02-2024
 */
public interface IReferralService {
    void setReferralEntity(YtmsUserDto userDto);
    List<ReferralDto> findByReferrerId(YtmsUserDto userDto);
    ResponseEntity sendEmailToReferral(String email) throws MessagingException;
    String getMyReferralLink();
}
