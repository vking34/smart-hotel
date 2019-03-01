package com.hust.smarthotel.components.hotel.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {

    private String type;

    private Double coordinates[];

}
