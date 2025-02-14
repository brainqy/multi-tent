package com.brainqy.api.services.IServices;

import com.brainqy.api.dto.ProfileCompletionDto;
import com.brainqy.api.dto.ResponseWrapperDto;
import com.brainqy.api.dto.YtmsUserDto;

import java.util.List;
import java.util.Map;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
public interface IYtmsUserService {

    YtmsUserDto createNewUser(YtmsUserDto userDto);

    YtmsUserDto getUserByEmailAdd(String emailAdd);

    List<YtmsUserDto> getAllPendingUsers();

    Boolean approvePendingUser(String emailAdd);

    Boolean declinePendingUser(String emailAdd);

    ResponseWrapperDto forgotPassword(String email);

    Boolean resetPassword(Map<String, String> map);

    ResponseWrapperDto changePassword(Map<String, String> map);

    ResponseWrapperDto getAllTrainers();
    String SetLoginHistory(String email);
    ProfileCompletionDto getProfileCompletion(YtmsUserDto userDto);

    List<YtmsUserDto> getAllUsers();
}
