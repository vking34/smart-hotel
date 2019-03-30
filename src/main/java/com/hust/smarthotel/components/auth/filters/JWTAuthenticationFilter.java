package com.hust.smarthotel.components.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.smarthotel.components.auth.model.AuthenRequest;
import com.hust.smarthotel.components.auth.model.TokenResponse;
import com.hust.smarthotel.components.user.repository.UserRepository;
import com.hust.smarthotel.generic.constant.HeaderConstant;
import com.hust.smarthotel.generic.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.hust.smarthotel.generic.response.ErrorResponses.FAIL_AUTHEN_RESPONSE;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static TokenResponse SUCCESS_RESPONSE = new TokenResponse(true);

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenRequest userRequest = OBJECT_MAPPER.readValue(request.getInputStream(), AuthenRequest.class);
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User userContext = (User) authResult.getPrincipal();
        String username = userContext.getUsername();

        com.hust.smarthotel.components.user.domain_model.User user = userRepository.findUserByUsername(username);
        SUCCESS_RESPONSE.setUser(user);

        String token = jwtUtil.createToken(user);
        SUCCESS_RESPONSE.setToken(token);

        response.addHeader(HeaderConstant.CONTENT_TYPE, HeaderConstant.APP_JSON);
        response.addHeader(HeaderConstant.AUTHORIZATION, HeaderConstant.TOKEN_PREFIX + token);
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(SUCCESS_RESPONSE));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.addHeader(HeaderConstant.CONTENT_TYPE, HeaderConstant.APP_JSON);
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(FAIL_AUTHEN_RESPONSE));
    }
}
