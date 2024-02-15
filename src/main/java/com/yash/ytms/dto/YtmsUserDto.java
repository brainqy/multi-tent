package com.yash.ytms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yash.ytms.constants.UserAccountStatusTypes;
import lombok.Data;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Data
public class YtmsUserDto {

    private String fullName;

    private String emailAdd;

    private String password;

    private String confirmPassword;

    private UserAccountStatusTypes accountStatus;
    private Integer coins=0;
    private String ref;
    private String refto;

    private UserRoleDto userRole;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @JsonProperty
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
