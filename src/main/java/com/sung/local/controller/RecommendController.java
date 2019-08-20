package com.sung.local.controller;

import com.sung.local.dto.RecommendReponseDto;
import com.sung.local.dto.RecommendRequestDto;
import com.sung.local.dto.SupportInfoDto;
import com.sung.local.service.RecommendInterface;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 *
 * @author 123msn
 * @since 2019-08-21
 */
@RestController
@RequestMapping("api/recommend")
public class RecommendController {

    @Autowired
    RecommendInterface recommendInterface;

    @PostMapping("region")
    @ApiOperation(value ="지자체 추천 정보", notes = "요청 시 거리 가중치가 항상 랜덤하게 변경됩니다.")
    public RecommendReponseDto getRecommendRegion(
            @RequestBody RecommendRequestDto recommendRequestDto
    ) {
        RecommendReponseDto recommendReponseDto = recommendInterface.getRecommendRegion(recommendRequestDto);

        return recommendReponseDto;
    }
}
