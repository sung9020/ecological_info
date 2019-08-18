package com.sung.local.security.impl;

import com.sung.local.dto.UserDto;
import com.sung.local.entity.User;
import com.sung.local.repository.UserRepository;
import com.sung.local.security.JwtProvider;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityExistsException;

import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class SecurityServiceTest {

    private final String username = "admin";
    private final String password = "5555";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    public void 가입_테스트(){
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(passwordEncoder.encode(password));
        String token = "";
        if(!userRepository.existsByUsername(username)){
            userRepository.save(userDto.toEntity());

            Authentication authentication = authenticationManager.authenticate(
                    new TestingAuthenticationToken(username, password));
            token = Optional.of(jwtProvider.createToken(authentication))
                    .orElseThrow(() -> new EntityExistsException(""));
        }else{
            fail();
        }

        assertThat(token.length(), Matchers.greaterThan(0));
    }

    @Test
    public void 로그인_테스트(){
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(passwordEncoder.encode(password));
        if(!userRepository.existsByUsername(username)){
            userRepository.save(userDto.toEntity());
        }else{
            fail();
        }

        Authentication authentication = authenticationManager.authenticate(
                new TestingAuthenticationToken(username, password)
        );
        String token = jwtProvider.createToken(authentication);

        assertThat(token.length(), Matchers.greaterThan(0));
    }

    @Test
    public void 토큰갱신_유니크_테스트(){
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(passwordEncoder.encode(password));
        if(!userRepository.existsByUsername(username)){
            userRepository.save(userDto.toEntity());
        }else{
            fail();
        }

        Authentication authentication = new TestingAuthenticationToken(username, password);
        String token_A = Optional.of(jwtProvider.createToken(authentication)).orElseThrow(() -> new EntityExistsException(""));

        authentication = jwtProvider.getAuthentication(token_A);
        String token_B = Optional.of(jwtProvider.createToken(authentication)).orElseThrow(() -> new EntityExistsException(""));

        assertThat(token_A, Matchers.not(token_B));
    }
}