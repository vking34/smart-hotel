package com.hust.smarthotel.generic.util;


import com.hust.smarthotel.components.user.domain_model.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {
    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";
    private static final String ROLE = "role";

    @Value("${security.jwt.expiry-in-milliseconds}")
    private int expiryInMilliSeconds;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

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


}
