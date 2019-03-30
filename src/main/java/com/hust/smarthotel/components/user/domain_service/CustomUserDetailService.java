package com.hust.smarthotel.components.user.domain_service;

import com.hust.smarthotel.components.user.domain_model.SysUser;
import com.hust.smarthotel.components.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static com.hust.smarthotel.generic.response.ErrorResponses.USER_NOT_FOUND_EXCEPTION;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_INACTIVE;
import static com.hust.smarthotel.generic.constant.RoleConstants.ROLE;


public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser user = userRepository.findSysUserByUsername(s);
        if (user == null)
            throw USER_NOT_FOUND_EXCEPTION;
        if (!user.getActive())
            throw USER_INACTIVE;

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(ROLE + user.getRole());
        return new User(s, user.getPassword(), authorities);
    }
}
