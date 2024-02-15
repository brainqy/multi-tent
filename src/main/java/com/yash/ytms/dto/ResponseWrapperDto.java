package com.yash.ytms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapperDto {

    private String status;

    private String message;

    private Object data;
}
