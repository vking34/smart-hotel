package com.hust.smarthotel.components.mananging.repository;

import com.hust.smarthotel.components.mananging.domain_model.Managing;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagingRepository extends MongoRepository<Managing, String>, ManagingRepositoryCustom {

    public Managing findManagingByUserIdAndAndHotelId(String managerId, ObjectId hotelId);

    public void deleteByUserIdAndAndHotelId(String managerId, ObjectId hotelId);

    public List<Managing> findManagingByUserId(String managerId);
}
