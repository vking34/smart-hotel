package com.hust.smarthotel.components.booking.app_model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @Field("user")
    @NotNull
    private BUser user;


    @Transient
    @Pattern(regexp = "^[a-z0-9]{24}$")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // not writing in json response
    private String hotelId;

    @Field("room_type")
    @Pattern(regexp = "^[0-9]{1,6}$")
    @NotNull
    private String roomType;

    @JsonProperty("rent_type")
    @Field("rent_type")
    @Positive
    @NotNull
    private Integer rentType;

    @Field("rent_value")
    @JsonProperty("rent_value")
    @NotNull
    private Integer rentValue;

    @Field("start_time")
    @JsonProperty("start_time")
    @NotNull
    private Integer startTime;

    @Field("end_time")
    @JsonProperty("end_time")
    @NotNull
    private Integer endTime;

    @Field("quantity")
    @Positive
    @NotNull
    private Integer quantity;

    @Field("checkin_date")
    @Future(message = "must be a future date")
    @NotNull
    private LocalDateTime checkinDate;

    @Field("checkout_date")
    @Future(message = "must be a future date and after check in date")
    @NotNull
    private LocalDateTime checkoutDate;

    public BookingRequest(BookingRequest request){
        this.user = request.user;
        this.hotelId = request.hotelId;
        this.roomType = request.roomType;
        this.rentType = request.rentType;
        this.rentValue = request.rentValue;
        this.startTime = request.startTime;
        this.endTime = request.endTime;
        this.quantity = request.quantity;
        this.checkinDate = request.checkinDate;
        this.checkoutDate = request.checkoutDate;
    }
}
