package com.hust.smarthotel.configs;

import com.hust.smarthotel.components.auth.filters.JWTAuthenticationFilter;
import com.hust.smarthotel.components.auth.filters.JWTAuthorizationFilter;
import com.hust.smarthotel.components.user.domain_service.CustomUserDetailService;
import com.hust.smarthotel.components.user.repository.UserRepository;
import com.hust.smarthotel.generic.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailService customUserDetailService;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.cors();
        http.cors().and().csrf().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), userRepository, jwtUtil));


        // filter all patterns start with /api/v1
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/{v1/:[a-zA-Z0-9\\/-?&=.]+}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/{v1/:[a-zA-Z0-9\\/-?&=.]+}").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/{v1/:[a-zA-Z0-9\\/-?&=.]+}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/{v1/:[a-zA-Z0-9\\/-?&=.]+}").permitAll()
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil));

    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring()
                .antMatchers("/signup")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/images/**")
        ;
    }

    // encode password before attempt authentication (searching for db)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/login", new CorsConfiguration().applyPermitDefaultValues());
//        source.registerCorsConfiguration("/api/{v1/:[a-zA-Z0-9\\/-?&=.]+}", new CorsConfiguration().applyPermitDefaultValues());
//        source.registerCorsConfiguration("/images/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
