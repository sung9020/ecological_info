package com.sung.local.service;

import com.sung.local.dto.InstitutionsDto;
import com.sung.local.dto.RegionDto;
import com.sung.local.dto.ResponseDto;
import com.sung.local.dto.SupportInfoDto;
import com.sung.local.entity.SupportInfo;

import java.util.List;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
public interface SupportInterface {

    ResponseDto setSupportInfo();
    List<SupportInfoDto> getSupportInfoList();
    SupportInfoDto getSupportInfo(String region);
    ResponseDto changeSupportInfo(SupportInfoDto supportInfoDto);
    RegionDto getRegionsByOrder(int pageCount);
    InstitutionsDto getMinRateInstitutions();



}
