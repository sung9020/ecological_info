package com.sung.local.security;

import com.sung.local.entity.User;
import com.sung.local.enums.ErrorFormat;
import com.sung.local.enums.Role;
import com.sung.local.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.of(userRepository.findByUsername(username))
                .orElseThrow(
                        () -> new UsernameNotFoundException(ErrorFormat.NOT_FOUND_USER_ERROR.getMsg())
                );

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(Role.ROLE_ADMIN.getRole()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
