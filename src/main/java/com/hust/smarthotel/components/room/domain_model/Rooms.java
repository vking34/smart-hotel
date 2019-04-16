package com.hust.smarthotel.components.room.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
//    @NotNull
//    @Pattern(regexp = "^[a-z0-9]{24}$")
    private String hotelId;

    @Field("rooms")
    @NotNull
    private List<Room> rooms;

    public Rooms(String hotelId) {
        this.hotelId = hotelId;
        this.rooms = new ArrayList<>();
    }

    public Rooms(String hotelId, List<Room> rooms){
        this.hotelId = hotelId;
        this.rooms = rooms;
    }
}
