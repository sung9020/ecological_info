package com.sung.local.dto;

import lombok.Getter;
import lombok.Setter;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Getter
@Setter
public class SupportInfoForRankDto extends SupportInfoDto {
    private long limitLong;

    private double interestSubsidyMin;

    private double interestSubsidyAvg;

    private double interestSubsidyMax;

}
