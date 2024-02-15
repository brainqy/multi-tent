package com.yash.ytms.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Project Name - ytms-api
 * <p>
 * IDE Used - IntelliJ IDEA
 *
 * @author - yash.raj
 * @since - 25-01-2024
 */
@Configuration
public class CommonConfigs {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
