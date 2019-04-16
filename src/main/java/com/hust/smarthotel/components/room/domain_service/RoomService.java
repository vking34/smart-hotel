package com.hust.smarthotel.components.room.domain_service;

import com.hust.smarthotel.components.room.app_model.RoomResponse;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import com.hust.smarthotel.components.room.domain_model.Rooms;
import com.hust.smarthotel.components.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static com.hust.smarthotel.generic.response.ErrorResponses.ROOM_EXISTS;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public Page<Rooms> findAll(Integer page, Integer pageSize){
        return roomRepository.findAll(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Rooms getRoomsInHotel(String hotelId){
        Rooms rooms = roomRepository.findRoomsByHotelId(hotelId);
        if (rooms == null)
            rooms = new Rooms(hotelId);
        return rooms;
    }

    public RoomResponse createRooms(String hotelId, Rooms rooms){
        if (roomRepository.findRoomsByHotelId(hotelId) != null)
            return ROOM_EXISTS;

        roomRepository.save(rooms);
        return new RoomResponse(true, null, null, rooms);
    }

}
