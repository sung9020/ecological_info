package com.sung.local.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Configuration
public class AppConfig {

    @Bean
    public ModelMapper setModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }
}
