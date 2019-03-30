package com.hust.smarthotel.components.user.repository;

import com.hust.smarthotel.components.user.domain_model.Manager;
import com.hust.smarthotel.components.user.domain_model.SysUser;
import com.hust.smarthotel.components.user.domain_model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{ username: ?0 }")
    SysUser findSysUserByUsername(String username);

    Manager findUserById(String id);

    User findUserByUsername(String username);

    @Query(value = "{}", fields = "{ full_name : 0 , email : 0 , phone : 0 }")
    Page<User> findAll(Pageable pageable);

    @Query(value = "{ '$or' : [ { username : ?0 }, { email : ?1 }, { phone : ?2 } ] }")
    User findUser(String username, String email, String phone);

    @Query(value = "{ '$or' : [ { email : ?0 }, { phone : ?1 } ] }", fields = "{ full_name : 0 , email : 0 , phone : 0 , picture : 0, }")
    List<User> findUserByEmailOrPhone(String email, String phone);

    @Query(value = "{ role: \"MANAGER\"}", fields = "{ password : 0 }")
    Page<Manager> findManagers(Pageable pageable);

    @Query(value = "{ role: \"CLIENT\"}", fields = "{ password : 0 }")
    Page<User> findClients(Pageable pageable);
}
