package com.sung.local.security;

import com.sung.local.enums.ErrorFormat;
import com.sung.local.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/*
 *
 * @author 123msn
 * @since 2019-08-18
 */
@Component
@Slf4j
@PropertySource("classpath:application.yml")
public class JwtProvider {

    @Value("${security.jwt.config.expire-in-milliseconds}")
    private long expireInMilliseconds;

    @Value("${security.jwt.config.signature-key}")
    private String signatureKey;

    private final String ROLE = "ROLE";

    private final String AUTH_KEYWORD = "authority";

    @Autowired
    private CustomUserDetails customUserDetails;

    @PostConstruct
    protected void init(){
        signatureKey = Base64.getEncoder().encodeToString(signatureKey.getBytes());
    }

    public String createToken(Authentication authentication){
        Claims claims = Jwts.claims();
        claims.put(ROLE, AuthorityUtils.createAuthorityList(Role.ROLE_ADMIN.getRole()));

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .setId(String.valueOf(UUID.randomUUID()))
                .signWith(SignatureAlgorithm.HS256, signatureKey)
                .compact();
    }

    private Claims getUserInfoByJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(signatureKey)
                .parseClaimsJws(token)

        return claims;
    }

    public Authentication getAuthentication(String token){
        Claims claims = getUserInfoByJWT(token);
        UserDetails userDetails = customUserDetails.loadUserByUsername(claims.getSubject());

        if(claims.get(ROLE) instanceof List){
            List<GrantedAuthority> grantedAuthorityList = getAuthListByToken((List) claims.get(ROLE));
            return new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuthorityList);
        }else{
            throw new IllegalArgumentException(ErrorFormat.FORBIDDEN_ERROR.getMsg());
        }
    }

    public boolean validateToken(String token){

        try {
            Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            throw new IllegalArgumentException(ErrorFormat.TOKEN_ERROR.getMsg());
        }

    }

    private List<GrantedAuthority> getAuthListByToken(List tokenAuthorityList){

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        int tokenAuthorityListSize = tokenAuthorityList.size();
        for(int i = 0; i < tokenAuthorityListSize; i++){
            if(tokenAuthorityList.get(i) instanceof Map){
                Map authMap = (Map) tokenAuthorityList.get(i);
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.valueOf(authMap.get(AUTH_KEYWORD)));
                    grantedAuthorityList.add(grantedAuthority);
            }
        }

        return grantedAuthorityList;
    }

}
