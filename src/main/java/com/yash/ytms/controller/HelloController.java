package com.yash.ytms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 23-06-2024
 */
@RestController
@RequestMapping
public class HelloController {
    @GetMapping("/hello")
    public String getHello(){
        return  "Hello User";
    }
}
