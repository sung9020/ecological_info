package com.sung.local.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

/*
 *
 * @author 123msn
 * @since 2019-08-20
 */
@Getter
@ApiModel
public class RecommendRequestDto {
    @ApiModelProperty(value = "input", example = "김진수씨는 안양 아현동에 살고 있는데, 은퇴하고 시설 관리 비즈니스를 하기를 원한다. \n" +
            "그에게 자금은 30억 정도를 보유하고 있고, \n" +
            "이차보전 비율은 1.5% 이내로 정하고 정부에서 운영하는 지자체 협약 지원정보를 검색한다.")
    String input;
}
