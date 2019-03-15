package com.hust.smarthotel.components.user.domain_service;


import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserAsyncImpl implements UserAsynTasks {

    @Autowired
    private UserRepository userRepository;

    @Async
    @Override
    public void updateUser(User client) {
        userRepository.save(client);
    }

    @Async
    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
