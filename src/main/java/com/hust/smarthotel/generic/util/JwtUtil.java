package com.hust.smarthotel.generic.util;


import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String ROLE = "role";

    @Value("${security.jwt.expiry-in-milliseconds}")
    private int expiryInMilliSeconds;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    public String createToken(User user){
        return Jwts.builder()
                .setSubject(user.getId())
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(USER_ID, user.getId())
                .claim(USERNAME, user.getUsername())
                .claim(ROLE, user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiryInMilliSeconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthorizationFromToken(String token){
        if (token == null)
            return null;

        String user_id = null;

        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            user_id = claims.getSubject();

        }catch (Exception e){
            return null;
        }
        UserDetails userDetails = null;
        if (user_id != null){
            userDetails = customUserDetailService.loadUserByUserId(user_id);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Claims getClaims(String token){
        if (token == null)
            return null;
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return claims;
    }

}
