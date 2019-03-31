package com.hust.smarthotel.components.booking.domain_model;

import com.hust.smarthotel.components.booking.app_model.BookingRequest;
import static com.hust.smarthotel.generic.constant.BookingState.NEW_CREATED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Field("status")
    private String status;

    public BookingRecord(BookingRequest bookingRequest){
        super(bookingRequest);
        this.createdDate = LocalDateTime.now();
        this.status = NEW_CREATED;
    }
}
