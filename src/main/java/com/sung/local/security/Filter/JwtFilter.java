package com.sung.local.security.Filter;

import com.sung.local.security.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String getJwtByRequest(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        String token = "";
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            token = bearer.substring(7);
        }
        return token;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtByRequest(request);

        if(!StringUtils.isEmpty(token) && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
