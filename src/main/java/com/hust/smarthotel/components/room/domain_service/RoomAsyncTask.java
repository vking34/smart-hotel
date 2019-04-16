package com.hust.smarthotel.components.room.domain_service;

import com.hust.smarthotel.components.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomAsyncTask {
    @Autowired
    private RoomRepository roomRepository;

    public void deleteRooms(String hotelId){
        roomRepository.deleteRoomsByHotelId(hotelId);
    }
}
