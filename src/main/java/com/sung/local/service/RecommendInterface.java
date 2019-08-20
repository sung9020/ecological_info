package com.sung.local.service;

import com.sung.local.dto.RecommendReponseDto;
import com.sung.local.dto.RecommendRequestDto;
import com.sung.local.dto.ResponseDto;

/*
 *
 * @author 123msn
 * @since 2019-08-19
 */
public interface RecommendInterface {
    RecommendReponseDto getRecommendRegion(RecommendRequestDto recommendRequestDto);
}
