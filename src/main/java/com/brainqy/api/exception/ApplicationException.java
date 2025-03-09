package com.brainqy.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Project Name - brainqy-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - Dnyaneshwar Somwanshi
 * @since - 25-01-2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private String message;
}
