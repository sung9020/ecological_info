package com.sung.local.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sung.local.entity.SupportInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
@Setter
public class SupportInfoDto {

    @JsonIgnore
    private long id;

    private String region;
    private String supportTarget;
    private String usage;
    private String supportLimit;
    private String rate;
    private String institutions;
    private String management;
    private String reception;

    @JsonIgnore
    private String regionCode;

    public SupportInfoDto(){
    }

    public SupportInfoDto(SupportInfo supportInfo){
        //this.id = supportInfo.getId();
        this.regionCode = supportInfo.getRegionCode();
        this.supportTarget = supportInfo.getSupportTarget();
        this.usage = supportInfo.getUsage();
        this.supportLimit = supportInfo.getSupportLimit();
        this.rate = supportInfo.getRate();
        this.institutions = supportInfo.getInstitutions();
        this.management = supportInfo.getManagement();
        this.reception = supportInfo.getReception();
    }

    public SupportInfo toEntity(){
        SupportInfo supportInfo = new SupportInfo();
        supportInfo.setRegionCode(regionCode);
        supportInfo.setSupportTarget(supportTarget);
        supportInfo.setUsage(usage);
        supportInfo.setSupportLimit(supportLimit);
        supportInfo.setRate(rate);
        supportInfo.setInstitutions(institutions);
        supportInfo.setManagement(management);
        supportInfo.setReception(reception);

        return supportInfo;
    }
}
