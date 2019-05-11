package com.hust.smarthotel.components.user.repository;

import com.hust.smarthotel.components.user.domain_model.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepository extends MongoRepository<Manager, String> {

    @Query(value = "{ '$or' : [ { username : ?0 }, { email : ?1 }, { phone : ?2 } ] }")
    List<Manager> findManager(String username, String email, String phone);

    @Query("{ status : ?0 , full_name: { '$regex': ?1 , '$options': 'i' }, phone: { '$regex': ?2 }}")
    Page<Manager> findAllByStatus(String status, String name, String phone ,Pageable pageable);

    Manager findManagerById(String id);

    Manager findManagerByIdAndStatus(String id, String status);
}
