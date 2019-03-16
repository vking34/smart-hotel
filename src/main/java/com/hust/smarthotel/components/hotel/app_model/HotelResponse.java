package com.hust.smarthotel.components.hotel.app_model;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
    public Boolean status;
    public String message;
    public Integer code;
    public Hotel hotel;
    public HotelResponse(Hotel hotel){
        this.status = true;
        this.hotel = hotel;
    }
}
