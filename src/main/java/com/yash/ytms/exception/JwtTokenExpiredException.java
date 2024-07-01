package com.yash.ytms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 01-07-2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenExpiredException extends  RuntimeException{
    private String message;
}
