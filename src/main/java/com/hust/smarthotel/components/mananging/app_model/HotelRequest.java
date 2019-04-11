package com.hust.smarthotel.components.mananging.app_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {

    @JsonProperty("hotel_id")
    @Pattern(regexp = "^[a-z0-9]{24}$")
    private String hotelId;
}
