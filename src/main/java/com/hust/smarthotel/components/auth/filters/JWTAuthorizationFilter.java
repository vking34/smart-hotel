package com.hust.smarthotel.components.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.hust.smarthotel.generic.response.ErrorResponses.INVALID_TOKEN;
import static com.hust.smarthotel.generic.response.ErrorResponses.MISSING_TOKEN;
import static com.hust.smarthotel.generic.constant.HeaderConstant.CONTENT_TYPE;
import static com.hust.smarthotel.generic.constant.HeaderConstant.APP_JSON;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JwtUtil jwtUtil;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authorizationField = request.getHeader(HeaderConstant.AUTHORIZATION);
        if (authorizationField == null || !authorizationField.startsWith(HeaderConstant.TOKEN_PREFIX)){
//            filterChain.doFilter(request, response);
            response.setStatus(403);
            response.addHeader(CONTENT_TYPE, APP_JSON);
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(MISSING_TOKEN));
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationField.replace(HeaderConstant.TOKEN_PREFIX, "");
        UsernamePasswordAuthenticationToken authenticationToken = jwtUtil.getAuthorizationFromToken(token);
        if (authenticationToken == null){
            response.setStatus(403);
            response.addHeader(CONTENT_TYPE, APP_JSON);
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(INVALID_TOKEN));
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
