package com.sung.local.service.impl;

import com.sung.local.dto.LocalGovernmentDto;
import com.sung.local.dto.SupportInfoDto;
import com.sung.local.dto.SupportInfoForRankDto;
import com.sung.local.entity.LocalGovernment;
import com.sung.local.entity.SupportInfo;
import com.sung.local.enums.ErrorFormat;
import com.sung.local.enums.FileFormat;
import com.sung.local.enums.Unit;
import com.sung.local.repository.LocalGovernmentRepository;
import com.sung.local.repository.SupportInfoRepository;
import com.sung.local.utils.FileUtils;
import jdk.nashorn.internal.ir.WhileNode;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/*
 *
 * @author 123msn
 * @since 2019-08-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class RecommendServiceTest {

    ModelMapper modelMapper;

    @Autowired
    LocalGovernmentRepository localGovernmentRepository;

    @Autowired
    SupportInfoRepository supportInfoRepository;

    private final String INPUT = "김진수씨는 안양 아현동에 살고 있는데, 은퇴하고 시설 관리 비즈니스를 하기를 원한다. " +
            "\n그에게 자금은 30억 정도를 보유하고 있고, " +
            "\n이차보전 비율은 1.5% 이내로 정하고 정부에서 운영하는 지자체 협약 지원정보를 검색한다";

    private final String UNIT_PATTERN = "\\d*(\\w+ ?)(억원|억|천만원|천|백만원|백)";
    private final String PERCENT_PATTERN = "\\d+(\\.\\d+)?%";
    private final String USAGE_PATTERN = "(운전|시설)";

    @Before
    public void setUp() throws Exception {
        setCsvData();
        modelMapper = new ModelMapper();
    }

    @Test
    public void 금융지원_지자체_추천(){
        SupportInfoDto result = new SupportInfoDto();

        List<LocalGovernmentDto> localGovernmentDtoList = localGovernmentRepository.findAll().stream()
                .map(LocalGovernmentDto::new).collect(Collectors.toList());

        List<String> regionList = localGovernmentDtoList.stream()
                .map(LocalGovernmentDto::getRegion).collect(Collectors.toList());

        List<String> regionCodeList = localGovernmentDtoList.stream()
                .map(LocalGovernmentDto::getRegionCode).collect(Collectors.toList());

        final List<String> INPUT_ARRAY = Arrays.asList(INPUT.split("\\s+"));

        String region = regionList.stream().filter(
                r -> INPUT_ARRAY.stream().anyMatch(w -> isSubstring(r,w))).findFirst().orElseThrow(()
                -> new IllegalArgumentException(ErrorFormat.NOT_FOUND_REGION_ERROR.getMsg()));

        double rate = getRateByString(INPUT);
        long limit = getLimitLongByString(INPUT);
        Set<String> usageSet = getUsageByString(INPUT);

        int[][] ranges =  getRanges(regionCodeList);
        setRangeRank(regionList, ranges, region);

        for(String tempRegion : regionList){
            if(!region.equals(tempRegion) || !region.contains(tempRegion)){
                LocalGovernment localGovernment = localGovernmentRepository.findByRegion(tempRegion);

                SupportInfo supportInfo = supportInfoRepository.findByRegionCode(localGovernment.getRegionCode());
                SupportInfoForRankDto supportInfoForRankDto = modelMapper.map(supportInfo, SupportInfoForRankDto.class);

                setSupportLimit(supportInfoForRankDto, supportInfo);
                if(limit > supportInfoForRankDto.getLimitLong()){
                    continue;
                }

                setRate(supportInfoForRankDto, supportInfo);
                if(rate < supportInfoForRankDto.getInterestSubsidyMin()
                        || rate > supportInfoForRankDto.getInterestSubsidyMax()) {
                    continue;
                }

                boolean usageFlag = isUsageYn(usageSet, supportInfoForRankDto.getUsage());
                if(!usageFlag) continue;

                result = supportInfoForRankDto;
                // 거리 가까운 순으로 조건에 맞으면 break;
                break;
            }
        }
        assertThat(result, Matchers.notNullValue());
    }

    private boolean isUsageYn(Set<String> usageSet, String regionUsage){
        boolean result = false;
        for(String usage : usageSet){
            if(!regionUsage.contains(usage)){
                result = false;
                break;
            }
            result = true;
        }
        return result;
    }

    private boolean isSubstring(String a, String b){

        int aLength = a.length();
        int bLength = b.length();
        int length = aLength > bLength ? bLength : aLength ;

        for (int i = 0; i < length; i++) {
            int index = aLength > bLength ? a.indexOf(b.charAt(i)) : b.indexOf(a.charAt(i));
            if (index < 0) {
                return false;
            } else {
                if (i > index) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<String> getUsageByString(String input){
        Pattern pattern = Pattern.compile(USAGE_PATTERN);
        Matcher matcher = pattern.matcher(input);
        Set<String> usageSet = new HashSet<>();
        while(matcher.find()){
            if(usageSet.size() < 2){
                usageSet.add(matcher.group());
            }else{
                break;
            }

        }
        return usageSet;
    }

    private double getRateByString(String input){
        Pattern pattern = Pattern.compile(PERCENT_PATTERN);
        Matcher matcher = pattern.matcher(input);
        String rateStr = "";
        if(matcher.find()){
            rateStr = matcher.group(0);
        }

        Double rate = Double.valueOf(rateStr.replaceAll("%", ""));
        return rate;
    }

    private long getLimitLongByString(String input){
        Pattern pattern = Pattern.compile(UNIT_PATTERN);
        Matcher matcher = pattern.matcher(input);
        String limitStr = "";
        if(matcher.find()){
            limitStr = matcher.group(0);
        }
        long result = 0L;

        for(Unit unit : Unit.values()){
            if(limitStr.contains(unit.getUnit())){
                String origin = limitStr.split(unit.getUnit())[0];
                if(origin.length() > 0){
                    result = Long.valueOf(origin) * unit.getWon();
                    break;
                }
            }
        }
        return result;
    }
    // 가중치에 따라 지역 정렬하기
    private void setRangeRank(List<String> regionList, int[][] ranges, String regionCode){
        List<String> copyList = new ArrayList<>(regionList);
        int index = copyList.indexOf(regionCode);
        regionList.sort(Comparator.comparing(str -> ranges[index][copyList.indexOf(str)]));
    }

    // 랜덤으로 지역간 거리 점수 주기 1~100
    private int[][] getRanges(List<String> regionList){
        Random random = new Random();
        int count = regionList.size();
        int[][] ranges = new int[count][count];
        Set<Integer> unique = new HashSet<>();

        for(int i = 0; i< count;i++){
            for(int j =0; j< count; j++){
                if(i != j && ranges[i][j] == 0){
                    int range = random.nextInt(100);
                    if(!unique.contains(range)){
                        ranges[i][j] = range;
                        ranges[j][i] = range;
                    }else{
                        j--;
                    }

                }
            }
        }

        return ranges;
    }

    private void setRate(SupportInfoForRankDto supportInfoForRankDto, SupportInfo supportInfo){
        String rateStr = supportInfo.getRate();

        if(rateStr.contains("~")){
            String[] rateArray = rateStr.split("~");
            supportInfoForRankDto.setInterestSubsidyMin(Double.valueOf(rateArray[0].replaceAll("%", "")));
            supportInfoForRankDto.setInterestSubsidyMax(Double.valueOf(rateArray[1].replaceAll("%", "")));
            supportInfoForRankDto.setInterestSubsidyAvg(
                    (supportInfoForRankDto.getInterestSubsidyMin() + supportInfoForRankDto.getInterestSubsidyMax()) / 2.0
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