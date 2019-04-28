package com.hust.smarthotel.components.hotel.app_model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelStatus {
    @NotNull
    private Boolean status;
}
