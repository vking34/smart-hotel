package com.hust.smarthotel.components.user.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import com.hust.smarthotel.components.user.app_model.ManagerResponse;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.components.user.domain_model.Manager;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.repository.UserRepository;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hust.smarthotel.generic.constant.RoleConstants.*;
import static com.hust.smarthotel.generic.response.ErrorResponses.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserAsynTasks asynTasks;

    public Manager findUser(String userId){
        return userRepository.findUserById(userId);
    }

    public Page<User> findAll(Integer page, Integer pageSize){
        return userRepository.findAll(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Page<Manager> findManagers(Integer page, Integer pageSize){
        return userRepository.findManagers(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Page<User> findClients(Integer page, Integer pageSize){
        return userRepository.findClients(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public UserResponse createClient(User client){
        User user = userRepository.findUser(client.getUsername(), client.getEmail(), client.getPhone());
        if (user != null)
            return USER_EXISTS;

        client.setRole(CLIENT);
        client.setActive(true);
        user = userRepository.save(client);
        return new UserResponse(true, null,null, user);
    }

    public ManagerResponse createManger(Manager requestManager){
        User user = userRepository.findUser(requestManager.getUsername(), requestManager.getEmail(), requestManager.getPhone());
        if (user != null)
            return MANAGER_RESPONSE;
        String hotelId = requestManager.getHotelId();
        if (!hotelId.equals("")){
            Hotel hotel = hotelRepository.findHotelById(hotelId);
            if (hotel == null) return HOTEL_NOT_EXISTING;
        }

        requestManager.setRole(MANAGER);
        requestManager.setActive(true);
        Manager manager = userRepository.save(requestManager);
        return new ManagerResponse(true, null, null, manager);
    }

    public UserResponse updateClient(String userId, User requestUser){
        User user = userRepository.findUserById(userId);
        if (user == null)
            return USER_NOT_FOUND;
        List<User> users = userRepository.findUserByEmailOrPhone(requestUser.getEmail(), requestUser.getPhone());
        if (users.size() > 1)
            return EMAIL_PHONE_EXISTS;
        user.setName(requestUser.getName());
        user.setFullName(requestUser.getFullName());
        user.setEmail(requestUser.getEmail());
        user.setPhone(requestUser.getPhone());
        user.setPicture(requestUser.getPicture());
        asynTasks.updateClient(user);
        return new UserResponse(true, null, null, user);
    }

    public ManagerResponse deleteUser(String userId){
        Manager user = userRepository.findUserById(userId);
        if (user == null)
            return MANAGER_NOT_FOUND;
        asynTasks.deleteUser(user);
        return new ManagerResponse(true, null, null, user);
    }
}
