package com.sung.local.service;

import com.sung.local.dto.LocalGovernmentDto;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
public interface LocalGovernmentInterface {
    LocalGovernmentDto getLocalGovernment(String region);
}
