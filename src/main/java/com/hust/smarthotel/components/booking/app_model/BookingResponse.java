package com.hust.smarthotel.components.booking.app_model;


import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Boolean status;
    private String message;
    private Integer code;
    private BookingRecord bookingRecord;
}
