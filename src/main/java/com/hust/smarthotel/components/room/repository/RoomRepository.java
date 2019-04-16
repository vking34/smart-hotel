package com.hust.smarthotel.components.room.repository;

import com.hust.smarthotel.components.room.domain_model.Rooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<Rooms, String> {
    public Page<Rooms> findAll(Pageable pageable);

    public Rooms findRoomsByHotelId(String hotelId);

    public void deleteRoomsByHotelId(String hotelId);
}
