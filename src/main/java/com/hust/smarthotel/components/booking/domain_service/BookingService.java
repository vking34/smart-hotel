package com.hust.smarthotel.components.booking.domain_service;

import com.hust.smarthotel.components.booking.app_model.BookingRequest;
import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.booking.app_model.DetailBookingResponse;
import com.hust.smarthotel.components.booking.app_model.StateRequest;
import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import com.hust.smarthotel.components.booking.repository.BookingRepository;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.domain_service.ManagingService;
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
import java.util.ArrayList;
import java.util.List;


import static com.hust.smarthotel.generic.constant.BookingState.CANCELED;
import static com.hust.smarthotel.generic.constant.BookingState.NEW_CREATED;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_FETCHED;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_INVALID_ROOM_TYPE;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_INVALID_RENT_TYPE;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_REQUEST_COMPLETED;
import static com.hust.smarthotel.generic.response.ErrorResponses.BOOKING_NOT_FETCHED;

@Service
public class BookingService {

    private static final String CREATED_DATE = "created_date";

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ManagingService managingService;

    @Autowired
    private BookingAsyncTask asyncTask;

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

        if (priceList == null)
            return BOOKING_INVALID_ROOM_TYPE;


        boolean rentTypeValidation = false;

        for (Price price : priceList){
            if (price.getRentType().equals(bookingRequest.getRentType())
            && price.getRentValue().equals(bookingRequest.getRentValue())
            && price.getStartTime().equals(bookingRequest.getStartTime())
            && price.getEndTime().equals(bookingRequest.getEndTime())){
                bookingRecord.setRentName(price.getName());
                bookingRecord.setPrice(price.getPrice() * bookingRecord.getQuantity());
                rentTypeValidation = true;
                break;
            }
        }

        if (!rentTypeValidation)
            return BOOKING_INVALID_RENT_TYPE;

        bookingRecord.setHotel(hotel);
        bookingRepository.save(bookingRecord);

        return new DetailBookingResponse(true, null, null, bookingRecord);
    }

    public DetailBookingRecord findDetailBookingRecordById(String id){
        DetailBookingRecord record = bookingRepository.findDetailBookingRecordById(id);
//        if (record == null)
//            record = new DetailBookingRecord();
        return record;
    }

    public BookingRecord findBookingRecordById(String id){
        return bookingRepository.findBookingRecordById(id);
    }

    public DetailBookingRecord changeState(DetailBookingRecord bookingRecord, StateRequest stateRequest){

        if (stateRequest.getPrice() != null && !bookingRecord.getPrice().equals(stateRequest.getPrice())){
            bookingRecord.setPrice(stateRequest.getPrice());
        }

        bookingRecord.setStatus(stateRequest.getStatus());
        bookingRecord.setUpdatedTime(LocalDateTime.now());

        return bookingRepository.save(bookingRecord);
    }

    public Page<DetailBookingRecord> findBookingRecordsByUserId(String userId, Integer page, Integer pageSize){
        if (page == null)
            page = 0;
        if (pageSize == null)
            pageSize = 10;

        return bookingRepository.findBookingRecordsOfUser(userId, page, pageSize);
    }

    public DetailBookingResponse cancelBookingRequest(DetailBookingRecord bookingRecord){
        if (!bookingRecord.getStatus().equals(NEW_CREATED))
            return BOOKING_REQUEST_COMPLETED;

        bookingRecord.setStatus(CANCELED);
        asyncTask.updateBookingRecord(bookingRecord);
        return new DetailBookingResponse(true, null, null, bookingRecord);
    }

    public BookingResponse fetchByClient(BookingRecord bookingRecord){
        if (bookingRecord.getStatus().equals(NEW_CREATED))
            return BOOKING_NOT_FETCHED;

        bookingRecord.setClientFetched(true);
        asyncTask.updateBookingRecord(bookingRecord);
        return new BookingResponse(true, null, null, bookingRecord);
    }

    public BookingResponse fetchByHotel(BookingRecord bookingRecord){
        if (!bookingRecord.getStatus().equals(NEW_CREATED)){
            if (!bookingRecord.getHotelFetched()){
                bookingRecord.setHotelFetched(true);
                asyncTask.updateBookingRecord(bookingRecord);
            }
            return BOOKING_FETCHED;
        }

        bookingRecord.setHotelFetched(true);
        asyncTask.updateBookingRecord(bookingRecord);
        return new BookingResponse(true, null, null, bookingRecord);
    }

    public Page<DetailBookingRecord> findBookingRecordsNotFetchedByClient(String userId, Integer page, Integer pageSize){
        if (page == null)
            page = 0;
        if (pageSize == null)
            pageSize = 10;

        return bookingRepository.findBookingRecordsNotFetchedByClient(userId, page, pageSize);
    }

    public Page<BookingRecord> findBookingRecordsNotFetchedByHotel(String hotelId, Integer page, Integer pageSize){
        if (page == null)
            page = 0;
        if (pageSize == null)
            pageSize = 10;

        return bookingRepository.findBookingRecordsNotFetchedByHotel(new ObjectId(hotelId), PageRequestCreator.getDescPageRequest(page, pageSize, CREATED_DATE));
    }

    public Page<BookingRecord> getBookingRecords(){
        return bookingRepository.findAll(PageRequestCreator.getSimplePageRequest(0,10));
    }

    public Page<BookingRecord> findBookingRecordsOfHotel(String hotelId, Integer page, Integer pageSize){
        return bookingRepository.findBookingRecordsByHotelRef(new ObjectId(hotelId), PageRequestCreator.getDescPageRequest(page, pageSize, CREATED_DATE));
    }

    public Page<BookingRecord> findBookingList(String managerId, Integer page, Integer pageSize){
        List<Managing> managingList = managingService.findManagingByManagerId(managerId);

        List<ObjectId> hotelList = new ArrayList<>();
        for (Managing managing: managingList) {
            hotelList.add(managing.getHotelId());
        }

        return bookingRepository.findBookingRecordsByHotelRefIsIn(hotelList, PageRequestCreator.getDescPageRequest(page, pageSize, CREATED_DATE));
    }
}
