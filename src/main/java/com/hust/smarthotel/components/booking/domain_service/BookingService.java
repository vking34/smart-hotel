package com.hust.smarthotel.components.booking.domain_service;

import com.hust.smarthotel.components.booking.app_model.BookingRequest;
import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.booking.app_model.DetailBookingResponse;
import com.hust.smarthotel.components.booking.app_model.StateRequest;
import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import com.hust.smarthotel.components.booking.repository.BookingRepository;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import com.hust.smarthotel.components.room.domain_model.Price;
import com.hust.smarthotel.components.room.domain_model.Room;
import com.hust.smarthotel.components.room.domain_model.Rooms;
import com.hust.smarthotel.components.room.repository.RoomRepository;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    public DetailBookingResponse insert(BookingRequest bookingRequest, Hotel hotel){
        DetailBookingRecord bookingRecord = new DetailBookingRecord(bookingRequest);

        Rooms rooms = roomRepository.findRoomsByHotelId(bookingRequest.getHotelId());
        List<Room> roomList = rooms.getRooms();
        List<Price> priceList = null;
        for (Room room : roomList) {
            if (room.getRoomType().equals(bookingRequest.getRoomType())){
                bookingRecord.setRoomName(room.getName());
                priceList = room.getPrices();
                break;
            }
        }

        for (Price price : priceList){
            if (price.getRentType().equals(bookingRequest.getRentType())
            && price.getRentValue().equals(bookingRequest.getRentValue())
            && price.getStartTime().equals(bookingRequest.getStartTime())
            && price.getEndTime().equals(bookingRequest.getEndTime())){
                bookingRecord.setRentName(price.getName());
                bookingRecord.setPrice(price.getPrice());
                break;
            }
        }

        bookingRepository.save(bookingRecord);
        bookingRecord.setHotel(hotel);
        return new DetailBookingResponse(true, null, null, bookingRecord);
    }

    public BookingRecord findBookingRecordById(String id){
        BookingRecord record = bookingRepository.findBookingRecordById(id);
        if (record == null)
            record = new BookingRecord();
        return record;
    }

    public DetailBookingRecord findDetailBookingRecordById(String id){
        DetailBookingRecord record = bookingRepository.findDetailBookingRecordById(id);
        if (record == null)
            record = new DetailBookingRecord();
        return record;
    }

    public DetailBookingRecord changeState(DetailBookingRecord bookingRecord, StateRequest stateRequest){

        if (stateRequest.getPrice() != null && !bookingRecord.getPrice().equals(stateRequest.getPrice())){
            bookingRecord.setPrice(stateRequest.getPrice());
        }

        bookingRecord.setStatus(stateRequest.getStatus());
        bookingRecord.setUpdatedTime(LocalDateTime.now());

        return bookingRepository.save(bookingRecord);
    }

    public Page<BookingRecord> getBookingRecords(){
        return bookingRepository.findAll(PageRequestCreator.getSimplePageRequest(0,10));
    }

    public Page<DetailBookingRecord> findBookingRecordsByUserId(String userId, Integer page, Integer pageSize){
        return bookingRepository.findBookingRecordsOfUser(userId, PageRequestCreator.getSimplePageRequest(page, pageSize));
    }



}
