package com.sung.local.service.impl;

import com.sung.local.dto.LocalGovernmentDto;
import com.sung.local.dto.RecommendReponseDto;
import com.sung.local.dto.RecommendRequestDto;
import com.sung.local.dto.SupportInfoForRankDto;
import com.sung.local.entity.LocalGovernment;
import com.sung.local.entity.SupportInfo;
import com.sung.local.enums.ErrorFormat;
import com.sung.local.enums.Unit;
import com.sung.local.repository.LocalGovernmentRepository;
import com.sung.local.repository.SupportInfoRepository;
import com.sung.local.service.RecommendInterface;
import com.sung.local.service.SupportInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 *
 * @author 123msn
 * @since 2019-08-19
 */
@Service
public class RecommendService implements RecommendInterface {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LocalGovernmentRepository localGovernmentRepository;

    @Autowired
    SupportInfoRepository supportInfoRepository;

    @Autowired
    SupportInterface supportInterface;

    private final String UNIT_PATTERN = "\\d*(\\w+ ?)(억원|억|천만원|천|백만원|백)";
    private final String PERCENT_PATTERN = "\\d+(\\.\\d+)?%";
    private final String USAGE_PATTERN = "(운전|시설)";

    @Override
    public RecommendReponseDto getRecommendRegion(RecommendRequestDto recommendRequestDto) {
        final String INPUT = recommendRequestDto.getInput();

        RecommendReponseDto recommendReponseDto = new RecommendReponseDto();

        List<LocalGovernmentDto> localGovernmentDtoList = localGovernmentRepository.findAll().stream()
                .map(LocalGovernmentDto::new).collect(Collectors.toList());
        
        if(localGovernmentDtoList.size() < 1){
            throw new IllegalArgumentException(ErrorFormat.DATA_INPUT_ERROR.getMsg());
        }

        List<String> regionList = localGovernmentDtoList.stream()
                .map(LocalGovernmentDto::getRegion).collect(Collectors.toList());

        List<String> regionCodeList = localGovernmentDtoList.stream()
                .map(LocalGovernmentDto::getRegionCode).collect(Collectors.toList());

        // 기사에 나온 단어 중 지자체 이름이 포함되어있는지 찾는다.
        final List<String> INPUT_ARRAY = Arrays.asList(INPUT.split("\\s+"));
        String region = regionList.stream().filter(
                r -> INPUT_ARRAY.stream().anyMatch(w -> isSubstring(r,w))).findFirst().orElseThrow(()
                -> new IllegalArgumentException(ErrorFormat.NOT_FOUND_REGION_ERROR.getMsg()));
        // 이차보전, 지원금액, 용도를 파싱한다.
        double rate = getRateByString(INPUT);
        long limit = getLimitLongByString(INPUT);
        Set<String> usageSet = getUsageByString(INPUT);
        // 거리 점수에 따라 짧은 순대로 정렬한다.
        int[][] ranges =  getRanges(regionCodeList);
        setRangeRank(regionList, ranges, region);

        for(String tempRegion : regionList){
            // 현재 지역과 가까운 지역을 추천해보자.
            if(!region.equals(tempRegion) || !region.contains(tempRegion)){
                LocalGovernment localGovernment = localGovernmentRepository.findByRegion(tempRegion);

                SupportInfo supportInfo = supportInfoRepository.findByRegionCode(localGovernment.getRegionCode());
                SupportInfoForRankDto supportInfoForRankDto = modelMapper.map(supportInfo, SupportInfoForRankDto.class);

                supportInterface.setSupportLimit(supportInfoForRankDto, supportInfo);
                if(limit > supportInfoForRankDto.getLimitLong()){
                    continue;
                }
                supportInterface.setRate(supportInfoForRankDto, supportInfo);
                if(rate < supportInfoForRankDto.getInterestSubsidyMin()
                        || rate > supportInfoForRankDto.getInterestSubsidyMax()) {
                    continue;
                }
                boolean usageFlag = isUsageYn(usageSet, supportInfoForRankDto.getUsage());
                if(!usageFlag) continue;

                recommendReponseDto.setRegion(supportInfoForRankDto.getRegionCode());
                recommendReponseDto.setSupportLimit(supportInfoForRankDto.getSupportLimit());
                recommendReponseDto.setUsage(supportInfoForRankDto.getUsage());
                recommendReponseDto.setRate(supportInfoForRankDto.getRate());
                // 거리 가까운 순으로 조건에 맞으면 break;
                break;
            }
        }

        return recommendReponseDto;
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
    private void setRangeRank(List<String> regionList, int[][] ranges, String region){
        List<String> copyList = new ArrayList<>(regionList);
        int index = copyList.indexOf(region);
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
}
