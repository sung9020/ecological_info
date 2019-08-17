package com.sung.ecology.dto;

import com.sung.ecology.entity.LocalGovernment;
import lombok.Getter;

import javax.persistence.Column;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
public class LocalGovernmentDto {

    private String localGovernmentName;

    private String localGovernmentCode;

    public LocalGovernmentDto(){

    }

    public LocalGovernmentDto(LocalGovernment localGovernment){
        this.localGovernmentName = localGovernment.getLocalGovernmentName();
        this.localGovernmentCode = localGovernment.getLocalGovernmentCode();
    }
}
