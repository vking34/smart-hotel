package com.hust.smarthotel.components.room.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Room")
public class Rooms {

    @Id
    private String id;

    @Field("hotel_id")
    private String hotelId;

    @Field("rooms")
    private List<Room> rooms;

    public Rooms(String hotelId) {
        this.hotelId = hotelId;
        this.rooms = new ArrayList<>();
    }
}
