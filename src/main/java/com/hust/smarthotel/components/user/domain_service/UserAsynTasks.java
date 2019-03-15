package com.hust.smarthotel.components.user.domain_service;

import com.hust.smarthotel.components.user.domain_model.User;
import org.springframework.stereotype.Component;


@Component
public interface UserAsynTasks {

    void updateUser(User user);
    void deleteUser(User user);
}
