package com.hust.smarthotel.components.user.domain_service;

import com.hust.smarthotel.components.user.domain_model.Manager;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.repository.ManagerRepository;
import com.hust.smarthotel.components.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class UserAsyncTasks {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Async
    public void updateUser(User client) {
        userRepository.save(client);
    }

    @Async
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Async
    public void rejectManager(Manager manager){
        manager.reject();
        managerRepository.save(manager);
    }

    @Async
    public void rejectManager(Manager manager, String reason){
        manager.reject(reason);
        managerRepository.save(manager);
    }

    @Async
    public void verifyManager(Manager manager){
        manager.verify();
        managerRepository.save(manager);
    }
}
