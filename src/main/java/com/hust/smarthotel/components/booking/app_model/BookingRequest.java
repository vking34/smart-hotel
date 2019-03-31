package com.hust.smarthotel.components.booking.app_model;


import com.hust.smarthotel.components.user.domain_model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Future;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @Field("user")
    private User user;

    @Field("hotel_id")
    @Pattern(regexp = "^[a-z0-9]{24}$")
    private String hotelId;

    @Field("room_type")
    @Pattern(regexp = "^[A-Z0-9]{2,6}$")
    private String roomType;

    @Field("rent_type")
    @Pattern(regexp = "^[A-Z0-9]{2,5}$")
    private String rentType;

    @Field("quantity")
    @Positive
    private Integer quantity;

    @Field("checkin_date")
    @Future(message = "must be a future date")
    private LocalDateTime checkinDate;

    @Field("checkout_date")
    @Future(message = "must be a future date and after check in date")
    private LocalDateTime checkoutDate;

    public BookingRequest(BookingRequest request){
        this.user = request.user;
        this.hotelId = request.hotelId;
        this.roomType = request.roomType;
        this.rentType = request.rentType;
        this.quantity = request.quantity;
        this.checkinDate = request.checkinDate;
        this.checkoutDate = request.checkoutDate;
    }


}
