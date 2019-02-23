package com.hust.smarthotel.components.room.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "Room")
public class Rooms {

    @Id
    private String id;

    @Field("hotel_id")
    private String hotelId;

    @Field("rooms")
    private List<Room> rooms;
}
