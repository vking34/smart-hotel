package com.hust.smarthotel.components.publish.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.hust.smarthotel.generic.constant.ChannelConstant.BOOKING_REQUEST_ID;
import static com.hust.smarthotel.generic.constant.BookingState.STATUS;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelNotification {
    @JsonProperty(BOOKING_REQUEST_ID)
    private String bookingRequestId;

    @JsonProperty(STATUS)
    private String status;
}
