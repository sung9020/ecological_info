package com.sung.local.dto;

import lombok.Getter;
import lombok.Setter;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@Getter
@Setter
public class TokenInfoDto extends ResponseDto{
    private String username;
    private String token;
}
