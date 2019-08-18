package com.sung.local.dto;

import com.sung.local.entity.SupportInfo;
import com.sung.local.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@Getter
@Setter
public class UserDto {

    private String username;

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
