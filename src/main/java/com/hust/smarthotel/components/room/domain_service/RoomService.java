package com.hust.smarthotel.components.room.domain_service;

import com.hust.smarthotel.generic.util.PageRequestCreator;
import com.hust.smarthotel.components.room.domain_model.Rooms;
import com.hust.smarthotel.components.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public Page<Rooms> findAll(Integer page, Integer pageSize){
        return roomRepository.findAll(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Rooms getRoomsInHotel(String hotelId){
        return roomRepository.findRoomsByHotelId(hotelId);
    }
}
