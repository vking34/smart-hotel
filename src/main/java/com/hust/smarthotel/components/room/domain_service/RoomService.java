package com.hust.smarthotel.components.room.domain_service;

import com.hust.smarthotel.components.room.app_model.RoomRequest;
import com.hust.smarthotel.components.room.app_model.RoomResponse;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import com.hust.smarthotel.components.room.domain_model.Rooms;
import com.hust.smarthotel.components.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static com.hust.smarthotel.generic.response.ErrorResponses.ROOM_EXISTS;
import static com.hust.smarthotel.generic.response.ErrorResponses.ROOM_NOT_EXIST;


@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomAsyncTask asyncTask;

    public Page<Rooms> findAll(Integer page, Integer pageSize){
        return roomRepository.findAll(PageRequestCreator.getSimplePageRequest(page, pageSize));
    }

    public Rooms getRoomsInHotel(String hotelId){
        Rooms rooms = roomRepository.findRoomsByHotelId(hotelId);
        if (rooms == null)
            rooms = new Rooms(hotelId);
        return rooms;
    }

    public RoomResponse createRooms(String hotelId, RoomRequest roomRequest){
        if (roomRepository.findRoomsByHotelId(hotelId) != null)
            return ROOM_EXISTS;

        Rooms rooms = new Rooms(hotelId, roomRequest.getRooms());
        roomRepository.save(rooms);
        return new RoomResponse(true, null, null, rooms);
    }

    public RoomResponse updateRooms(String hotelId, RoomRequest roomRequest){
        Rooms rooms = roomRepository.findRoomsByHotelId(hotelId);
        if ( rooms == null)
            return ROOM_NOT_EXIST;

        rooms.setRooms(roomRequest.getRooms());
        roomRepository.save(rooms);
        return new RoomResponse(true, null, null, rooms);
    }

    public RoomResponse deleteRooms(String hotelId){
        Rooms rooms = roomRepository.findRoomsByHotelId(hotelId);
        if (rooms == null)
            return ROOM_NOT_EXIST;
        asyncTask.deleteRooms(hotelId);
        return new RoomResponse(true, null, null, rooms);
    }

}
