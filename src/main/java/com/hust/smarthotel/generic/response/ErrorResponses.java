package com.hust.smarthotel.generic.response;

import com.hust.smarthotel.components.auth.model.AuthResponse;
import com.hust.smarthotel.components.booking.app_model.BookingResponse;
import com.hust.smarthotel.components.booking.app_model.DetailBookingResponse;
import com.hust.smarthotel.components.hotel.app_model.HotelResponse;
import com.hust.smarthotel.components.mananging.app_model.ManagingResponse;
import com.hust.smarthotel.components.photo.app_model.PhotoResponse;
import com.hust.smarthotel.components.review.app_model.ReviewResponse;
import com.hust.smarthotel.components.room.app_model.RoomResponse;
import com.hust.smarthotel.components.user.app_model.ManagerResponse;
import com.hust.smarthotel.components.user.app_model.UserResponse;
import com.hust.smarthotel.generic.model.ErrorResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponses {
    public static final ErrorResponse NOT_FOUND = new ErrorResponse("Not found entity by id", "NOTFOUND", 101);
    public static final AuthResponse FAIL_AUTHEN_RESPONSE = new AuthResponse(false, "Username/Password is wrong", 401);
    public static final AuthResponse INVALID_TOKEN = new AuthResponse(false, "Invalid Access Token", 102);
    public static final AuthResponse MISSING_TOKEN = new AuthResponse(false, "Missing Access Token/Invalid Authorization Form", 103);

    public static final UsernameNotFoundException USER_NOT_FOUND_EXCEPTION = new UsernameNotFoundException("User not found");
    public static final UsernameNotFoundException USER_INACTIVE = new UsernameNotFoundException("User is inactive");

    public static final HotelResponse HOTEL_NOT_FOUND = new HotelResponse(false, "Hotel is not found", 301, null);
    public static final HotelResponse HOTEL_INVALID_COORDINATES = new HotelResponse(false, "Longitude <= 180 && Latitude <= 90", 303, null);
    public static final HotelResponse HOTEL_EXISTS = new HotelResponse(false, "Hotel/Phone number exists already", 304, null);
    public static final HotelResponse HOTEL_OUT_OF_MANAGING = new HotelResponse(false, "Manager already manages the allowed number of hotels", 305, null);
    public static final HotelResponse HOTEL_NOT_MANAGED_BY_THIS_MANAGER = new HotelResponse(false, "This manager does not manager this hotel", 302, null);

    public static final UserResponse USER_USERNAME_TAKEN = new UserResponse(false, "Username is taken", 211, null);
    public static final UserResponse USER_EMAIL_TAKEN = new UserResponse(false, "Email is taken", 212, null);
    public static final UserResponse USER_PHONE_TAKEN = new UserResponse(false, "Email is taken", 212, null);
    public static final UserResponse USER_NOT_FOUND = new UserResponse(false, "User not found", 202, null);
    public static final UserResponse USER_EMAIL_PHONE_EXISTS = new UserResponse(false, "Email/Phone exists", 203, null);
    public static final UserResponse USER_FORBIDDEN = new UserResponse(false, "Access token does not belong to this user", 204, null);
    public static final ManagerResponse MANAGER_USERNAME_TAKEN = new ManagerResponse(false, "Username is taken", 221, null);
    public static final ManagerResponse MANAGER_EMAIL_TAKEN = new ManagerResponse(false, "Email is taken", 222, null);
    public static final ManagerResponse MANAGER_PHONE_TAKEN = new ManagerResponse(false, "Phone number is taken", 223, null);
    public static final UserResponse MANAGER_NOT_FOUND = new UserResponse(false, "Manager not found", 224, null);

    public static final BookingResponse BOOKING_HOTEL_NOT_FOUND = new BookingResponse(false, "Target hotel is not found", 501, null);
    public static final BookingResponse BOOKING_INVALID_DATE = new BookingResponse(false, "Checkin Date must be before Checkout Date", 502, null);
    public static final BookingResponse BOOKING_FORBIDDEN_GETTING_RECORD = new BookingResponse(false, "Forbidden to get the booking record", 503, null);
    public static final BookingResponse BOOKING_NOT_FOUND = new BookingResponse(false, "Booking Request Not Found", 509, null);
    public static final BookingResponse BOOKING_NOT_FETCHED = new BookingResponse(false, "Booking Request is not confirmed yet", 510, null);
    public static final DetailBookingResponse BOOKING_RECORD_NOT_FOUND = new DetailBookingResponse(false, "The booking record is not found", 504, null);
    public static final DetailBookingResponse BOOKING_REQUEST_COMPLETED = new DetailBookingResponse(false, "The booking request is completed, not in the PROCESSING state", 505, null);
    public static final DetailBookingResponse BOOKING_FORBIDDEN_CANCELATION = new DetailBookingResponse(false, "The booking request does not belong to you", 506, null);
    public static final DetailBookingResponse BOOKING_INVALID_ROOM_TYPE = new DetailBookingResponse(false, "Invalid Room Type", 507, null);
    public static final DetailBookingResponse BOOKING_INVALID_RENT_TYPE = new DetailBookingResponse(false, "Invalid Rent Value/ Rent Type/ Start time/ End Time", 508, null);


    public static final ReviewResponse REVIEW_HOTEL_NOT_FOUND = new ReviewResponse(false, "Hotel not found", 701, null);

    public static final ManagingResponse MANAGING_USER_NOT_FOUND = new ManagingResponse(false, "User not found", 801, null);
    public static final ManagingResponse MANAGING_NOT_MANAGER = new ManagingResponse(false, "User is not a manager", 802, null);
    public static final ManagingResponse MANAGING_HOTEL_NOT_FOUND = new ManagingResponse(false, "Hotel not found", 803, null);
    public static final ManagingResponse MANAGING_EXISTING_RECORD = new ManagingResponse(false, "The manager manages this hotel already", 804, null);
    public static final ManagingResponse MANAGING_RECORD_NOT_FOUND = new ManagingResponse(false, "This manager does not manage this hotel", 805, null);
    public static final ManagingResponse MANAGING_INVALID_TOKEN = new ManagingResponse(false, "Invalid Token", 806, null);
    public static final ManagingResponse MANAGING_OUT_OF_HOTEL = new ManagingResponse(false, "A Manager can manage at most 5 hotels", 807, null);

    public static final RoomResponse ROOM_FORBIDDEN = new RoomResponse(false, "You are not the manager of this hotel", 901, null);
    public static final RoomResponse ROOM_EXISTS = new RoomResponse(false, "Hotel have rooms already", 902, null);
    public static final RoomResponse ROOM_NOT_EXIST = new RoomResponse(false, "Room does not exist", 903, null);

    public static final PhotoResponse PHOTO_HOTEL_NOT_MANAGER = new PhotoResponse(false, "Not manager", 600, null);
    public static final PhotoResponse PHOTO_HOTEL_NOT_MANAGING = new PhotoResponse(false, "This manager does not manage this hotel", 601, null);
    public static final PhotoResponse PHOTO_INTERNAL_ERROR = new PhotoResponse(false, "Internal Server Error: IOException", 602, null);
    public static final PhotoResponse PHOTO_HOTEL_NOT_FOUND = new PhotoResponse(false, "Hotel is not found", 603, null);
    public static final PhotoResponse PHOTO_INVALID_TYPE = new PhotoResponse(false, "Invalid type", 604, null);
    public static final PhotoResponse PHOTO_MAX_PHOTOS = new PhotoResponse(false, "Number of photos is maximum", 605, null);
    public static final PhotoResponse PHOTO_INDEX_OUT_OF_BOUND = new PhotoResponse(false, "Position is out of bounds", 606, null);
    public static final PhotoResponse PHOTO_INVALID_TOKEN = new PhotoResponse(false, "Invalid Access Token", 607, null);
    public static final PhotoResponse PHOTO_USER_NOT_FOUND = new PhotoResponse(false, "User not found", 608, null);
    public static final PhotoResponse PHOTO_NOT_IMAGE = new PhotoResponse(false, "Not image file", 609, null);
    public static final PhotoResponse PHOTO_OVER_SIZE = new PhotoResponse(false, "The image file is over size", 610, null);


}
