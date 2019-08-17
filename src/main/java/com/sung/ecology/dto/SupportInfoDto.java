package com.sung.ecology.dto;

import com.sung.ecology.entity.SupportInfo;
import lombok.Getter;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
public class SupportInfoDto {

    private long id;
    private String localGovernmentCode;
    private String supportTarget;
    private String usage;
    private String supportLimit;
    private String interestSubsidy;
    private String institutions;
    private String management;
    private String dealer;

    public SupportInfoDto(){
    }

    public SupportInfoDto(SupportInfo supportInfo){
        this.id = supportInfo.getId();
        this.localGovernmentCode = supportInfo.getLocalGovernmentCode();
        this.supportTarget = supportInfo.getSupportTarget();
        this.usage = supportInfo.getUsage();
        this.supportLimit = supportInfo.getSupportLimit();
        this.interestSubsidy = supportInfo.getInterestSubsidy();
        this.institutions = supportInfo.getInstitutions();
        this.management = supportInfo.getManagement();
        this.dealer = supportInfo.getDealer();
    }

    SupportInfo toEntity(){
        SupportInfo supportInfo = SupportInfo.builder()
                .localGovernmentCode(localGovernmentCode)
                .supportTarget(supportTarget)
                .usage(usage)
                .supportLimit(supportLimit)
                .interestSubsidy(interestSubsidy)
                .institutions(institutions)
                .management(management)
                .dealer(dealer)
                .build();

        return supportInfo;
    }
}
