package com.sung.local.dto;

import com.sung.local.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@Getter
@Setter
@ApiModel
public class UserDto {
    @ApiModelProperty(value = "username", example ="admin", position = 1)
    private String username;
    @ApiModelProperty(value = "password", example ="1234", position = 2)
    private String password;

    public UserDto(){

    }

    public UserDto(User User){
        this.username = User.getUsername();
        this.password = User.getPassword();
    }

    public User toEntity(){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }
}
