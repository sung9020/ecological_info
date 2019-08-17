package com.sung.ecology.dto;

import com.sung.ecology.entity.SupportEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
public class SupportDto {

    private long id;
    private String localGovernmentCode;
    private String supportTarget;
    private String usage;
    private String supportLimit;
    private String interestSubsidy;
    private String institutions;
    private String management;
    private String dealer;

    public SupportDto(){
    }

    public SupportDto(SupportEntity supportEntity){
        this.id = supportEntity.getId();
        this.localGovernmentCode = supportEntity.getLocalGovernmentCode();
        this.supportTarget = supportEntity.getSupportTarget();
        this.usage = supportEntity.getUsage();
        this.supportLimit = supportEntity.getSupportLimit();
        this.interestSubsidy = supportEntity.getInterestSubsidy();
        this.institutions = supportEntity.getInstitutions();
        this.management = supportEntity.getManagement();
        this.dealer = supportEntity.getDealer();
    }

    SupportEntity toEntity(){
        SupportEntity supportEntity = SupportEntity.builder()
                .localGovernmentCode(localGovernmentCode)
                .supportTarget(supportTarget)
                .usage(usage)
                .supportLimit(supportLimit)
                .interestSubsidy(interestSubsidy)
                .institutions(institutions)
                .management(management)
                .dealer(dealer)
                .build();

        return supportEntity;
    }
}
