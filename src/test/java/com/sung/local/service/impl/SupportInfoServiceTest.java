package com.sung.local.service.impl;

import com.sung.local.dto.LocalGovernmentDto;
import com.sung.local.dto.SupportInfoDto;
import com.sung.local.dto.SupportInfoForRankDto;
import com.sung.local.entity.LocalGovernment;
import com.sung.local.entity.SupportInfo;
import com.sung.local.enums.FileFormat;
import com.sung.local.enums.Unit;
import com.sung.local.repository.LocalGovernmentRepository;
import com.sung.local.repository.SupportInfoRepository;
import com.sung.local.utils.FileUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class SupportInfoServiceTest {

    ModelMapper modelMapper;

    @Autowired
    LocalGovernmentRepository localGovernmentRepository;

    @Autowired
    SupportInfoRepository supportInfoRepository;


    @Before
    public void setUp() throws Exception {
        setCsvData();
        modelMapper = new ModelMapper();
    }

    @After
    public void tearDown() throws Exception {
        localGovernmentRepository.deleteAll();
        supportInfoRepository.deleteAll();
    }

    @Test
    public void 지자체_지원정보_출력하기(){
        List<SupportInfoDto> supportInfoDtoList = supportInfoRepository.findAll().stream()
                .map(SupportInfoDto::new).collect(Collectors.toList());

        assertThat(supportInfoDtoList, Matchers.hasSize(98));
    }

    @Test
    public void 지자체_지원정보_검색하기(){
        LocalGovernment localGovernment = localGovernmentRepository.findByRegion("강릉시");

        SupportInfo supportInfo = supportInfoRepository.findByRegionCode(localGovernment.getRegionCode());
        SupportInfoDto supportInfoDto = modelMapper.map(supportInfo, SupportInfoDto.class);
        assertThat(supportInfoDto.getManagement(), Matchers.equalTo("강릉지점"));
    }

    @Test
    public void 지자체_지원정보_수정하기(){
        SupportInfoDto requestDto = new SupportInfoDto();
        requestDto.setRegion("강릉시"); /* 지원 단체 이름 수정불가 */
        requestDto.setSupportTarget("강릉시에서 제주시로 이전한 지점");
        requestDto.setReception("강릉시 소재 대리점");

        LocalGovernment localGovernment = localGovernmentRepository.findByRegion(requestDto.getRegion());
        String regionCode = localGovernment.getRegionCode();
        SupportInfo supportInfo = supportInfoRepository.findByRegionCode(regionCode);
        supportInfo.setSupportTarget(requestDto.getSupportTarget());
        supportInfo.setReception(requestDto.getReception());
        supportInfoRepository.save(supportInfo);

        SupportInfo result = supportInfoRepository.findByRegionCode(regionCode);
        assertThat(result.getSupportTarget(), Matchers.equalTo("강릉시에서 제주시로 이전한 지점"));
    }

    @Test
    public void 지자체_지원정보_내림차순_조회(){
        int pageLimit = 10;
        List<SupportInfo> supportInfoList = supportInfoRepository.findAll();
        List<SupportInfoForRankDto> supportInfoForRankDtoList = new ArrayList<>();
        for(SupportInfo supportInfo : supportInfoList){
            SupportInfoForRankDto supportInfoForRankDto = modelMapper.map(supportInfo, SupportInfoForRankDto.class);
            setSupportLimit(supportInfoForRankDto, supportInfo);
            setRate(supportInfoForRankDto, supportInfo);

            supportInfoForRankDtoList.add(supportInfoForRankDto);
        }
        supportInfoForRankDtoList = supportInfoForRankDtoList.stream()
                .sorted(Comparator.comparing(SupportInfoForRankDto::getLimitLong).reversed()
                        .thenComparing(SupportInfoForRankDto::getInterestSubsidyAvg))
                .limit(pageLimit)
                .collect(Collectors.toList());

        assertThat(supportInfoForRankDtoList, Matchers.hasSize(10));
        assertThat(supportInfoForRankDtoList.get(0).getSupportLimit(), Matchers.equalTo("300억원 이내"));
        assertThat(supportInfoForRankDtoList.get(1).getSupportLimit(), Matchers.equalTo("90억원 이내"));
    }

    @Test
    public void 지자체_이차보전_비율_최소(){
        List<SupportInfo> supportInfoList = supportInfoRepository.findAll();
        List<SupportInfoForRankDto> supportInfoForRankDtoList = new ArrayList<>();
        for(SupportInfo supportInfo : supportInfoList){
            SupportInfoForRankDto supportInfoForRankDto = modelMapper.map(supportInfo, SupportInfoForRankDto.class);
            setRate(supportInfoForRankDto, supportInfo);

            supportInfoForRankDtoList.add(supportInfoForRankDto);
        }
        SupportInfoForRankDto supportInfoForRankDto = supportInfoForRankDtoList.stream()
                .filter(data -> data.getInterestSubsidyAvg() > 0.0)
                .min(Comparator.comparing(SupportInfoForRankDto::getInterestSubsidyAvg))
                .get();

        assertThat(supportInfoForRankDto.getInstitutions(), Matchers.equalTo("안양상공회의소"));
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

    private void setCsvData(){
        File file = new File("local_government_support_info.csv");
        List<List<String>> data = FileUtils.readCsv(file);


        int rowSize = data.size();

        for(int i = 1; i < rowSize; i++){
            List<String> row = data.get(i);
            String localGovernmentCode = "LGM" + String.format("%03d", i);

            LocalGovernmentDto localGovernmentDto = new LocalGovernmentDto();
            localGovernmentDto.setRegion(row.get(FileFormat.LOCAL_GOVERNMENT.getCol()));
            localGovernmentDto.setRegionCode(localGovernmentCode);
            localGovernmentRepository.save(localGovernmentDto.toEntity());

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
    }



}