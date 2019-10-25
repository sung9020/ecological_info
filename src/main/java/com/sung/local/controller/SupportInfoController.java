package com.sung.local.controller;

import com.sung.local.dto.InstitutionsDto;
import com.sung.local.dto.RegionDto;
import com.sung.local.dto.ResponseDto;
import com.sung.local.dto.SupportInfoDto;
import com.sung.local.service.SupportInterface;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@RestController
@RequestMapping("api/supportInfo")
public class SupportInfoController {

    @Autowired
    SupportInterface supportInterface;

    @PostMapping("")
    @ApiOperation(value ="지자체 협약 지원정보 추가(*중요)")
    public ResponseDto setSupportInfo() {
        ResponseDto response = supportInterface.setSupportInfo();

        return response;
    }

    @GetMapping("")
    @ApiOperation(value ="지자체 협약 지원정보 전체 조회")
    public List<SupportInfoDto> getSupportInfoList() {
        List<SupportInfoDto> supportInfoDtoList = supportInterface.getSupportInfoList();

        return supportInfoDtoList;
    }


    @GetMapping("{region}")
    @Cacheable(value = "region", key = "#region")
    @ApiOperation(value ="특정 지자체 협약지원정보 검색")
    public SupportInfoDto getSupportInfo(
            @ApiParam(required = true, name="region", value = "지자체 이름", example = "강릉시") @PathVariable("region") String region
    )  {
        SupportInfoDto supportInfoDto = supportInterface.getSupportInfo(region);

        return supportInfoDto;
    }

    @PutMapping("")
    @CacheEvict(value = "region", allEntries = true)
    @ApiOperation(value ="지자체 협약 지원정보 수정")
    public ResponseDto changeSupportInfo(
            @RequestBody SupportInfoDto supportInfoDto
    ) {
        ResponseDto response = supportInterface.changeSupportInfo(supportInfoDto);

        return response;
    }

    @GetMapping("/page/{pageCount}")
    @ApiOperation(value ="지원금액, 이차보전 순의 지자체 정보 검색")
    public RegionDto getRegionsByOrder(
            @ApiParam(required = true, name="pageCount", value = "조회 페이지 갯수", example = "10") @PathVariable("pageCount") int pageCount
    ){
        RegionDto RegionDto = supportInterface.getRegionsByOrder(pageCount);

        return RegionDto;
    }

    @GetMapping("rate/min/institutions")
    @ApiOperation(value ="최소 이차보전 비율의 추천기관 이름")
    public InstitutionsDto getMinRateInstitutions()  {
        InstitutionsDto institutionsDto = supportInterface.getMinRateInstitutions();

        return institutionsDto;
    }
}
