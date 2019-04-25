package com.hust.smarthotel.components.booking.app_model;

import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailBookingResponse {
    private Boolean status;
    private String message;
    private Integer code;
    private DetailBookingRecord bookingRecord;
}
