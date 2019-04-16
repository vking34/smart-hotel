package com.hust.smarthotel.generic.response;

import com.hust.smarthotel.components.auth.model.FailAuthenResponse;
import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.mananging.app_model.ManagingResponse;
import com.hust.smarthotel.components.review.app_model.ReviewResponse;
import com.hust.smarthotel.components.room.app_model.RoomResponse;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.generic.model.ErrorResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponses {
    public static final ErrorResponse NOT_FOUND = new ErrorResponse("Not found entity by id", "NOTFOUND", 101);
    public static final FailAuthenResponse FAIL_AUTHEN_RESPONSE = new FailAuthenResponse(false, "Username/Password is wrong", 401);

    public static final UsernameNotFoundException USER_NOT_FOUND_EXCEPTION = new UsernameNotFoundException("User not found");
    public static final UsernameNotFoundException USER_INACTIVE = new UsernameNotFoundException("User is inactive");


    public static final HotelResponse HOTEL_INVALID_COORDINATES = new HotelResponse(false, "Longitude <= 180 && Latitude <= 90", 303, null);
    public static final HotelResponse HOTEL_EXISTS = new HotelResponse(false, "Hotel/Phone number exists already", 304, null);

    public static final UserResponse USER_EXISTS = new UserResponse(false, "Username/Email/Phone exists", 201, null);
    public static final UserResponse USER_NOT_FOUND = new UserResponse(false, "User not found", 204, null);
    public static final UserResponse USER_EMAIL_PHONE_EXISTS = new UserResponse(false, "Username/Email/Phone exists", 201, null);


    public static final BookingResponse BOOKING_HOTEL_NOT_FOUND = new BookingResponse(false, "Target hotel is not found", 501, null);
    public static final BookingResponse BOOKING_INVALID_DATE = new BookingResponse(false, "Checkin Date must be before Checkout Date", 502, null);
    public static final BookingResponse BOOKING_FORBIDDEN_GETTING_RECORD = new BookingResponse(false, "Forbidden to get the booking record", 601, null);


    public static final ReviewResponse REVIEW_HOTEL_NOT_FOUND = new ReviewResponse(false, "Hotel not found", 701, null);

    public static final ManagingResponse MANAGING_USER_NOT_FOUND = new ManagingResponse(false, "User not found", 801, null);
    public static final ManagingResponse MANAGING_NOT_MANAGER = new ManagingResponse(false, "User is not a manager", 802, null);
    public static final ManagingResponse MANAGING_HOTEL_NOT_FOUND = new ManagingResponse(false, "Hotel not found", 803, null);
    public static final ManagingResponse MANAGING_EXISTING_RECORD = new ManagingResponse(false, "The manager manages this hotel already", 804, null);

    public static final RoomResponse ROOM_FORBIDDEN = new RoomResponse(false, "You are not the manager of this hotel", 901, null);
    public static final RoomResponse ROOM_EXISTS = new RoomResponse(false, "Hotel have rooms already", 902, null);
    public static final RoomResponse ROOM_NOT_EXIST = new RoomResponse(false, "Room does not exist", 903, null);
}
