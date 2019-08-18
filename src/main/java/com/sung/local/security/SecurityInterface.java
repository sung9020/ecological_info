package com.sung.local.security;

import com.sung.local.dto.TokenInfoDto;
import com.sung.local.dto.UserDto;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
public interface SecurityInterface {
    TokenInfoDto signIn(UserDto userDto);
    TokenInfoDto signUp(UserDto userDto);
    TokenInfoDto refresh();
}
