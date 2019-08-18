package com.sung.local.controller;

import com.sung.local.dto.TokenInfoDto;
import com.sung.local.dto.UserDto;
import com.sung.local.security.SecurityInterface;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@RestController
@RequestMapping("api/auth")
public class SecurityController {

    @Autowired
    SecurityInterface securityInterface;

    @PutMapping("refresh")
    @ApiOperation(value ="토큰 갱신")
    public TokenInfoDto refresh(){

        TokenInfoDto response = securityInterface.refresh();

        return response;
    }

    @PostMapping("signUp")
    @ApiOperation(value ="회원가입")
    public TokenInfoDto signUp(
            @ApiParam(required = true, name="유저 정보") @RequestBody UserDto userDto){

        TokenInfoDto response = securityInterface.signUp(userDto);

        return response;
    }

    @PostMapping("signIn")
    @ApiOperation(value ="로그인")
    public TokenInfoDto signIn(
            @ApiParam(required = true, name="유저 정보") @RequestBody UserDto userDto){

        TokenInfoDto response = securityInterface.signIn(userDto);

        return response;
    }

}
