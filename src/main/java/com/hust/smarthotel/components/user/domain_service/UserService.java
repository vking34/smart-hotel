package com.hust.smarthotel.components.user.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import com.hust.smarthotel.components.user.app_model.UpdatedUser;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.repository.UserRepository;
import com.hust.smarthotel.generic.util.EncryptedPasswordUtils;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hust.smarthotel.generic.constant.RoleConstants.*;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_EXISTS;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_NOT_FOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_EMAIL_PHONE_EXISTS;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserAsynTasks asynTasks;

    public User findUser(String userId){
        return userRepository.findUserById(userId);
    }

    public Page<User> findAll(Integer page, Integer pageSize){
        return userRepository.findAll(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Page<User> findManagers(Integer page, Integer pageSize){
        return userRepository.findManagers(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Page<User> findClients(Integer page, Integer pageSize){
        return userRepository.findClients(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public UserResponse createClient(User client){
        return createUser(client, CLIENT);
    }

    public UserResponse createManager(User manger){
        return createUser(manger, MANAGER);
    }

    private UserResponse createUser(User client, String role){
        User user = userRepository.findUser(client.getUsername(), client.getEmail(), client.getPhone());
        if (user != null)
            return USER_EXISTS;

        client.setRole(role);
        client.setPassword(EncryptedPasswordUtils.encryptPassword(client.getPassword()));
        client.setActive(true);
        user = userRepository.save(client);
        return new UserResponse(true, null,null, user);
    }

    public UserResponse updateClient(String userId, UpdatedUser requestUser){
        User user = userRepository.findUserById(userId);
        if (user == null)
            return USER_NOT_FOUND;
        List<User> users = userRepository.findUserByEmailOrPhone(requestUser.getEmail(), requestUser.getPhone());
        if (users.size() > 1)
            return USER_EMAIL_PHONE_EXISTS;
        user.setName(requestUser.getName());
        user.setFullName(requestUser.getFullName());
        user.setEmail(requestUser.getEmail());
        user.setPhone(requestUser.getPhone());
        asynTasks.updateUser(user);
        return new UserResponse(true, null, null, user);
    }

    public UserResponse deleteUser(String userId){
        User user = userRepository.findUserById(userId);
        if (user == null)
            return USER_NOT_FOUND;
        asynTasks.deleteUser(user);
        return new UserResponse(true, null, null, user);
    }
}