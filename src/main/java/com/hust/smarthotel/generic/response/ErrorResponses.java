package com.hust.smarthotel.generic.response;

import com.hust.smarthotel.components.auth.model.FailAuthenResponse;
import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.review.app_model.ReviewResponse;
import com.hust.smarthotel.components.user.app_model.ManagerResponse;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.generic.model.ErrorResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponses {
    public static final ErrorResponse NOT_FOUND = new ErrorResponse("Not found entity by id", "NOTFOUND", 101);

    public static final HotelResponse INVALID_COORDINATES = new HotelResponse(false, "Longitude <= 180 && Latitude <= 90", 303, null);



    public static final UserResponse USER_EXISTS = new UserResponse(false, "Username/Email/Phone exists", 201, null);
    public static final UserResponse USER_NOT_FOUND = new UserResponse(false, "User not found", 204, null);
    public static final ManagerResponse MANAGER_NOT_FOUND = new ManagerResponse(false, "User not found", 204, null);
    public static final UserResponse EMAIL_PHONE_EXISTS = new UserResponse(false, "Username/Email/Phone exists", 201, null);
    public static final ManagerResponse MANAGER_RESPONSE = new ManagerResponse(false, "Username/Email/Phone exists", 202, null);
    public static final ManagerResponse HOTEL_NOT_EXISTING = new ManagerResponse(false, "The hotel does not exists", 203, null);
    public static final ManagerResponse ADD_HOTEL_NOT_ALLOW = new ManagerResponse(false, "Your role is not allowed to add hotel", 206, null);

    public static final UsernameNotFoundException USER_NOT_FOUND_EXCEPTION = new UsernameNotFoundException("User not found");
    public static final UsernameNotFoundException USER_INACTIVE = new UsernameNotFoundException("User is inactive");


    public static final FailAuthenResponse FAIL_AUTHEN_RESPONSE = new FailAuthenResponse(false, "Username/Password is wrong", 401);

    public static final BookingResponse BOOKING_HOTEL_NOT_FOUND = new BookingResponse(false, "Target hotel is not found", 501, null);
    public static final BookingResponse BOOKING_INVALID_DATE = new BookingResponse(false, "Checkin Date must be before Checkout Date", 502, null);

    public static final BookingResponse FORBIDDEN_GETTING_BOOKING_RECORD = new BookingResponse(false, "Forbidden to get the booking record", 601, null);

    public static final ReviewResponse REVIEW_HOTEL_NOT_FOUND = new ReviewResponse(false, "Hotel not found", 701, null);
}
