package com.hust.smarthotel.components.booking.domain_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hust.smarthotel.components.booking.app_model.BookingRequest;
import static com.hust.smarthotel.generic.constant.BookingState.NEW_CREATED;

import com.hust.smarthotel.generic.util.ObjectIdSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Reservation")
public class BookingRecord extends BookingRequest {

    @Id
    private String id;

    @Field("created_date")
    private LocalDateTime createdDate;

    @Field("updated_time")
    private LocalDateTime updatedTime;

    @Field("status")
    private String status;

    @Field("room_name")
    private String roomName;

    @Field("rent_name")
    private String rentName;

    @Field("price")
    private Integer price;

    @Field("hotel_ref")
    @JsonProperty("hotel_id")
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId hotelRef;


    public BookingRecord(BookingRequest bookingRequest){
        super(bookingRequest);
        this.hotelRef = new ObjectId(bookingRequest.getHotelId());
        this.createdDate = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        this.status = NEW_CREATED;
    }
}
