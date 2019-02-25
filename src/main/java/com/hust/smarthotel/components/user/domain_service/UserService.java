package com.hust.smarthotel.components.user.domain_service;

import com.hust.smarthotel.components.user.domain_model.SysUser;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.repository.UserRepository;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> findAll(Integer page, Integer pageSize){
        return userRepository.findAll(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Page<SysUser> findSysUsers(Integer page, Integer pageSize){
        return userRepository.findSysUsers(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }
}
