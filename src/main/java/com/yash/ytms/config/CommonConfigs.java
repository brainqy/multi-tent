package com.yash.ytms.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

   /** @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    } */

   @Bean
   public ModelMapper modelMapper() {
       ModelMapper modelMapper = new ModelMapper();
       modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

       // Custom mapping for LocalDate and LocalTime
       modelMapper.createTypeMap(String.class, LocalDate.class)
               .setConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ISO_LOCAL_DATE));
       modelMapper.createTypeMap(String.class, LocalTime.class)
               .setConverter(ctx -> LocalTime.parse(ctx.getSource(), DateTimeFormatter.ofPattern("HH:mm a")));

       modelMapper.createTypeMap(LocalDate.class, String.class)
               .setConverter(ctx -> ctx.getSource().format(DateTimeFormatter.ISO_LOCAL_DATE));
       modelMapper.createTypeMap(LocalTime.class, String.class)
               .setConverter(ctx -> ctx.getSource().format(DateTimeFormatter.ofPattern("HH:mm a")));

       return modelMapper;
   }
}
