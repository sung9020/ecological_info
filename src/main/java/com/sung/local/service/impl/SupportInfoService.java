package com.sung.local.service.impl;

import com.sung.local.dto.*;
import com.sung.local.entity.LocalGovernment;
import com.sung.local.entity.SupportInfo;
import com.sung.local.enums.ErrorFormat;
import com.sung.local.enums.FileFormat;
import com.sung.local.enums.Unit;
import com.sung.local.repository.SupportInfoRepository;
import com.sung.local.service.LocalGovernmentInterface;
import com.sung.local.service.SupportInterface;
import com.sung.local.utils.FileUtils;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Service
public class SupportInfoService implements SupportInterface {

    @Autowired
    SupportInfoRepository supportInfoRepository;

    @Autowired
    LocalGovernmentInterface localGovernmentInterface;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseDto setSupportInfo() {
        ResponseDto responseDto = new ResponseDto();

        List<SupportInfo> supportInfoList = supportInfoRepository.findAll();
        if(supportInfoList.size() > 0){
            responseDto.setMsg(ErrorFormat.ALREADY_REGISTERED_FILE_ERROR.getMsg());
        }else{
            File file = new File("local_government_support_info.csv");
            List<List<String>> data = FileUtils.readCsv(file);

            int rowSize = data.size();

            for(int i = 1; i < rowSize; i++){
                List<String> row = data.get(i);
                String localGovernmentCode = "LGM" + String.format("%03d", i);

                SupportInfoDto supportInfoDto = new SupportInfoDto();
                supportInfoDto.setRegionCode(localGovernmentCode);
                supportInfoDto.setSupportTarget(row.get(FileFormat.SUPPORT_TARGET.getCol()));
                supportInfoDto.setUsage(row.get(FileFormat.USAGE.getCol()));
                supportInfoDto.setSupportLimit(row.get(FileFormat.SUPPORT_LIMIT.getCol()));
                supportInfoDto.setRate(row.get(FileFormat.INTEREST_SUBSIDY.getCol()));
                supportInfoDto.setInstitutions(row.get(FileFormat.INSTITUTIONS.getCol()));
                supportInfoDto.setManagement(row.get(FileFormat.MANAGEMENT.getCol()));
                supportInfoDto.setReception(row.get(FileFormat.DEALER.getCol()));

                supportInfoRepository.save(supportInfoDto.toEntity());
            }

            responseDto.setMsg(ErrorFormat.SUCCESS.getMsg());
        }

        return responseDto;
    }

    @Override
    public List<SupportInfoDto> getSupportInfoList() {

        List<SupportInfo> supportInfoList = supportInfoRepository.findAll();
        if(supportInfoList.size() < 1){
            throw new IllegalArgumentException(ErrorFormat.DATA_INPUT_ERROR.getMsg());
        }

        List<SupportInfoDto> supportInfoDtoList = supportInfoRepository.findAll().stream()
                .map(SupportInfoDto::new).collect(Collectors.toList());

        return supportInfoDtoList;
    }

    @Override
    public SupportInfoDto getSupportInfo(String region) {

        List<SupportInfo> supportInfoList = supportInfoRepository.findAll();
        if(supportInfoList.size() < 1){
            throw new IllegalArgumentException(ErrorFormat.DATA_INPUT_ERROR.getMsg());
        }

        LocalGovernmentDto localGovernmentDto = localGovernmentInterface.getLocalGovernment(region);

        SupportInfo supportInfo = supportInfoRepository.findByRegionCode(localGovernmentDto.getRegionCode());
        SupportInfoDto supportInfoDto = modelMapper.map(supportInfo, SupportInfoDto.class);

        return supportInfoDto;
    }

    @Override
    public ResponseDto changeSupportInfo(SupportInfoDto supportInfoDto) {
        LocalGovernmentDto localGovernmentDto = localGovernmentInterface.getLocalGovernment(supportInfoDto.getRegion());
        ResponseDto responseDto = new ResponseDto();

        if(Optional.ofNullable(localGovernmentDto).isPresent()){
            String regionCode = localGovernmentDto.getRegionCode();
            SupportInfo supportInfo = supportInfoRepository.findByRegionCode(regionCode);

            supportInfo.setSupportTarget(supportInfoDto.getSupportTarget());
            supportInfo.setUsage(supportInfoDto.getUsage());
            supportInfo.setSupportLimit(supportInfoDto.getSupportLimit());
            supportInfo.setRate(supportInfoDto.getRate());
            supportInfo.setInstitutions(supportInfoDto.getInstitutions());
            supportInfo.setManagement(supportInfoDto.getManagement());
            supportInfo.setReception(supportInfoDto.getReception());

            supportInfoRepository.save(supportInfo);
            responseDto.setMsg(ErrorFormat.SUCCESS.getMsg());
        }else{
            responseDto.setMsg(ErrorFormat.NOT_FOUND_REGION_ERROR.getMsg());
        }

        return responseDto;
    }

    @Override
    public RegionDto getRegionsByOrder(int pageCount) {

        List<SupportInfo> supportInfoList = supportInfoRepository.findAll();
        if(supportInfoList.size() < 1){
            throw new IllegalArgumentException(ErrorFormat.DATA_INPUT_ERROR.getMsg());
        }

        RegionDto regionDto = new RegionDto();
        List<SupportInfoForRankDto> supportInfoForRankDtoList = new ArrayList<>();
        for(SupportInfo supportInfo : supportInfoList){
            SupportInfoForRankDto supportInfoForRankDto = modelMapper.map(supportInfo, SupportInfoForRankDto.class);
            setSupportLimit(supportInfoForRankDto, supportInfo);
            setRate(supportInfoForRankDto, supportInfo);

            supportInfoForRankDtoList.add(supportInfoForRankDto);
        }
        regionDto.setRegions(
                supportInfoForRankDtoList.stream()
                .sorted(Comparator.comparing(SupportInfoForRankDto::getLimitLong).reversed()
                        .thenComparing(SupportInfoForRankDto::getInterestSubsidyAvg))
                .limit(pageCount)
                .map(SupportInfoDto::getRegion)
                .collect(Collectors.toList())
        );

        return regionDto;
    }

    @Override
    public InstitutionsDto getMinRateInstitutions() {

        List<SupportInfo> supportInfoList = supportInfoRepository.findAll();
        if(supportInfoList.size() < 1){
            throw new IllegalArgumentException(ErrorFormat.DATA_INPUT_ERROR.getMsg());
        }

        InstitutionsDto institutionsDto = new InstitutionsDto();
        List<SupportInfoForRankDto> supportInfoForRankDtoList = new ArrayList<>();
        for(SupportInfo supportInfo : supportInfoList){
            SupportInfoForRankDto supportInfoForRankDto = modelMapper.map(supportInfo, SupportInfoForRankDto.class);
            setRate(supportInfoForRankDto, supportInfo);

            supportInfoForRankDtoList.add(supportInfoForRankDto);
        }

        institutionsDto.setInstitutions(
                supportInfoForRankDtoList.stream()
                        .filter(data -> data.getInterestSubsidyAvg() > 0.0)
                        .min(Comparator.comparing(SupportInfoForRankDto::getInterestSubsidyAvg))
                        .map(SupportInfoDto::getInstitutions)
                        .get()
        );
        return institutionsDto;
    }

    private void setRate(SupportInfoForRankDto supportInfoForRankDto, SupportInfo supportInfo){
        String rateStr = supportInfo.getRate();

        if(rateStr.contains("~")){
            String[] rateArray = rateStr.split("~");
            supportInfoForRankDto.setInterestSubsidyMin(Double.valueOf(rateArray[0].replaceAll("%", "")));
            supportInfoForRankDto.setInterestSubsidyMax(Double.valueOf(rateArray[1].replaceAll("%", "")));
            supportInfoForRankDto.setInterestSubsidyAvg(
                    supportInfoForRankDto.getInterestSubsidyMin() + supportInfoForRankDto.getInterestSubsidyMax() / 2.0
            );
        }else{
            if(rateStr.contains("%")){
                Double rate = Double.valueOf(rateStr.replaceAll("%", ""));
                supportInfoForRankDto.setInterestSubsidyMin(rate);
                supportInfoForRankDto.setInterestSubsidyMax(rate);
                supportInfoForRankDto.setInterestSubsidyAvg(rate);
            }
        }
    }

    private void setSupportLimit(SupportInfoForRankDto supportInfoForRankDto, SupportInfo supportInfo){
        String supportLimitStr = supportInfo.getSupportLimit().split(" 이내")[0];
        long result = 0L;

        for(Unit unit : Unit.values()){
            if(supportLimitStr.contains(unit.getUnit())){
                String origin = supportLimitStr.split(unit.getUnit())[0];
                if(origin.length() > 0){
                    result = Long.valueOf(origin) * unit.getWon();
                }
            }
        }

        supportInfoForRankDto.setLimitLong(result);
    }
}
