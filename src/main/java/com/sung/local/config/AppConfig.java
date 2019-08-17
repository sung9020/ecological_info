package com.sung.local.config;

import org.modelmapper.ModelMapper;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
public class AppConfig {

    public ModelMapper setModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }
}
