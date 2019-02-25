package com.hust.smarthotel.components.user.repository;

import com.hust.smarthotel.components.user.domain_model.SysUser;
import com.hust.smarthotel.components.user.domain_model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Page<User> findAll(Pageable pageable);

    @Query("{}")
    Page<SysUser> findSysUsers(Pageable pageable);

}
