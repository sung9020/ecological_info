package com.sung.local.dto;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sung.local.entity.LocalGovernment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
@Setter
public class LocalGovernmentDto {

    private String region;
    private String regionCode;

    public LocalGovernmentDto(){

    }

    public LocalGovernment toEntity(){
        LocalGovernment localGovernment = new LocalGovernment();
        localGovernment.setRegion(region);
        localGovernment.setRegionCode(regionCode);

        return localGovernment;
    }

    public LocalGovernmentDto(LocalGovernment localGovernment){
        this.region = localGovernment.getRegion();
        this.regionCode = localGovernment.getRegionCode();
    }
}
