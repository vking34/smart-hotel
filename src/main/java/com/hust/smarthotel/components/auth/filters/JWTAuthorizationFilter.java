package com.hust.smarthotel.components.auth.filters;

import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtUtil jwtUtil;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authorizationField = request.getHeader(HeaderConstant.AUTHORIZATION);
        if (authorizationField == null || !authorizationField.startsWith(HeaderConstant.TOKEN_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        UsernamePasswordAuthenticationToken authenticationToken = jwtUtil.getAuthorizationFromToken(token);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
