package com.yash.ytms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YtmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtmsApplication.class, args);
    }

}
