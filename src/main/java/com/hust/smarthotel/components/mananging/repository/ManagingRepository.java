package com.hust.smarthotel.components.mananging.repository;

import com.hust.smarthotel.components.mananging.domain_model.Managing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagingRepository extends MongoRepository<Managing, String> {

    public Managing findManagingByUserIdAndAndHotelId(String managerId, String hotelId);

    public void deleteByUserIdAndAndHotelId(String managerId, String hotelId);
}
