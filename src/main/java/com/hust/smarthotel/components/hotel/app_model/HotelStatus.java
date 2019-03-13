package com.hust.smarthotel.components.hotel.app_model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class HotelStatus {
    private String message;

    @NotNull
    private Boolean status;
}
