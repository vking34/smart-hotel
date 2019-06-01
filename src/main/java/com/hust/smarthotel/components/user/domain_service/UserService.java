package com.hust.smarthotel.components.user.domain_service;

import com.hust.smarthotel.components.hotel.app_model.ManagerStatus;
import com.hust.smarthotel.components.user.app_model.ManagerResponse;
import com.hust.smarthotel.components.user.app_model.UpdatedUser;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.components.user.domain_model.BasicManager;
import com.hust.smarthotel.components.user.domain_model.Manager;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.repository.ManagerRepository;
import com.hust.smarthotel.components.user.repository.UserRepository;
import com.hust.smarthotel.generic.util.EncryptedPasswordUtils;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hust.smarthotel.generic.constant.RoleConstants.*;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_USERNAME_TAKEN;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_EMAIL_TAKEN;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_PHONE_TAKEN;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_NOT_FOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.USER_EMAIL_PHONE_EXISTS;
import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGER_USERNAME_TAKEN;
import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGER_EMAIL_TAKEN;
import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGER_PHONE_TAKEN;
import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGER_NOT_FOUND;
import static com.hust.smarthotel.generic.constant.ManagerState.VERIFYING;
import static com.hust.smarthotel.generic.constant.ManagerState.REJECTED;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserAsyncTasks asyncTasks;

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
        List<User> userList = userRepository.findUser(client.getUsername(), client.getEmail(), client.getPhone());
        User user;
        if (userList.size() != 0){
            user = userList.get(0);
            if (client.getUsername().equals(user.getUsername()))
                return USER_USERNAME_TAKEN;
            if (client.getEmail().equals(user.getEmail()))
                return USER_EMAIL_TAKEN;
            return USER_PHONE_TAKEN;
        }


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
        int numOfUsers = users.size();
        if (numOfUsers > 1 || (numOfUsers == 1 && !users.get(0).getId().equals(userId)))
            return USER_EMAIL_PHONE_EXISTS;

        user.setName(requestUser.getName());
        user.setFullName(requestUser.getFullName());
        user.setEmail(requestUser.getEmail());
        user.setPhone(requestUser.getPhone());
        asyncTasks.updateUser(user);
        return new UserResponse(true, null, null, user);
    }

    public UserResponse deleteUser(String userId){
        User user = userRepository.findUserById(userId);
        if (user == null)
            return USER_NOT_FOUND;
        asyncTasks.deleteUser(user);
        return new UserResponse(true, null, null, user);
    }

    public ManagerResponse requestVerifyingManager(BasicManager basicManager){
        List<Manager> managerList = managerRepository.findManager(basicManager.getUsername(), basicManager.getEmail(), basicManager.getPhone());
        Manager manager;
        if (managerList.size() != 0){
            manager = managerList.get(0);
            if (basicManager.getUsername().equals(manager.getUsername()))
                return MANAGER_USERNAME_TAKEN;
            if (basicManager.getEmail().equals(manager.getEmail()))
                return MANAGER_EMAIL_TAKEN;
            return MANAGER_PHONE_TAKEN;
        }

        List<User> userList = userRepository.findUser(basicManager.getUsername(), basicManager.getEmail(), basicManager.getPhone());
        if (userList.size() != 0){
            User user = userList.get(0);
            if (basicManager.getUsername().equals(user.getUsername()))
                return MANAGER_USERNAME_TAKEN;
            if (basicManager.getEmail().equals(user.getEmail()))
                return MANAGER_EMAIL_TAKEN;
            return MANAGER_PHONE_TAKEN;
        }

        manager = new Manager(basicManager);
        manager = managerRepository.save(manager);

        return new ManagerResponse(true, null, null, manager);
    }

    public Page<Manager> findVerifyingManagers(Integer page, Integer pageSize, String name, String phone){
        if (name == null)
            name = "";
        if (phone == null)
            phone = "";

        Page<Manager> managerPage = managerRepository.findAllByStatus(VERIFYING, name, phone , PageRequestCreator.getAscPageRequest(page, pageSize, "created_time"));
        if (managerPage == null){
            managerPage = new PageImpl<>(new ArrayList<>());
        }

        return managerPage;
    }

    public Manager findManager(String managerId){
        Manager manager = managerRepository.findManagerById(managerId);
        if (manager == null)
            manager = new Manager();
        return manager;
    }

    public UserResponse verifyManager(String managerId, ManagerStatus managerStatus){
        Manager manager = managerRepository.findManagerByIdAndStatus(managerId, VERIFYING);

        if (manager == null)
            return MANAGER_NOT_FOUND;

        if (managerStatus.getStatus().equals(REJECTED)){
            asyncTasks.rejectManager(manager);
            return new UserResponse(true, null, null, null);
        }

        List<User> userList = userRepository.findUser(manager.getUsername(), manager.getEmail(), manager.getPhone());
        if (userList.size() != 0){
            User user = userList.get(0);
            if (manager.getUsername().equals(user.getUsername())){
                asyncTasks.rejectManager(manager, "username is taken");
                return USER_USERNAME_TAKEN;
            }

            if (manager.getEmail().equals(user.getEmail())){
                asyncTasks.rejectManager(manager, "email is taken");
                return USER_EMAIL_TAKEN;
            }
            asyncTasks.rejectManager(manager, "phone is taken");
            return USER_PHONE_TAKEN;
        }

        asyncTasks.verifyManager(manager);
        User user = new User(manager);
        user = userRepository.save(user);
        return new UserResponse(true, null, null, user);
    }
}