package com.hust.smarthotel.components.room.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Room {

    private String roomType;
    private String name;
    private String description;
    private Integer availableRooms;
    private List<Price> prices;
}
