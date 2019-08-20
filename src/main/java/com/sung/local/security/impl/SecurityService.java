package com.sung.local.security.impl;

import com.sung.local.dto.TokenInfoDto;
import com.sung.local.dto.UserDto;
import com.sung.local.enums.ErrorFormat;
import com.sung.local.repository.UserRepository;
import com.sung.local.security.JwtProvider;
import com.sung.local.security.SecurityInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Optional;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@Service
public class SecurityService implements SecurityInterface {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public TokenInfoDto signUp(UserDto userDto) {
        TokenInfoDto tokenInfoDto = new TokenInfoDto();
        if(!userRepository.existsByUsername(userDto.getUsername())){
            String password = userDto.getPassword();
            userDto.setPassword(passwordEncoder.encode(password));
            userRepository.save(userDto.toEntity());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), password));

            tokenInfoDto.setUsername(userDto.getUsername());
            tokenInfoDto.setToken(
                    Optional.of(jwtProvider.createToken(authentication))
                    .orElseThrow(() -> new EntityExistsException("")));
            tokenInfoDto.setMsg(ErrorFormat.SUCCESS.getMsg());
        }else{
            tokenInfoDto.setUsername(userDto.getUsername());
            tokenInfoDto.setToken("");
            tokenInfoDto.setMsg(ErrorFormat.ALREADY_REGISTERED_USER_ERROR.getMsg());
        }
        return tokenInfoDto;
    }
    @Override
    public TokenInfoDto signIn(UserDto userDto) {
        TokenInfoDto tokenInfoDto = new TokenInfoDto();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );

        tokenInfoDto.setUsername(userDto.getUsername());
        tokenInfoDto.setToken(Optional.of(jwtProvider.createToken(authentication))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorFormat.NOT_FOUND_USER_ERROR.getMsg()))
        );
        tokenInfoDto.setMsg(ErrorFormat.SUCCESS.getMsg());

        return tokenInfoDto;
    }

    @Override
    public TokenInfoDto refresh() {
        TokenInfoDto tokenInfoDto = new TokenInfoDto();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        tokenInfoDto.setUsername(authentication.getName());
        tokenInfoDto.setToken(Optional.of(jwtProvider.createToken(authentication))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorFormat.NOT_FOUND_USER_ERROR.getMsg()))
        );
        tokenInfoDto.setMsg(ErrorFormat.SUCCESS.getMsg());

        return tokenInfoDto;
    }
}
