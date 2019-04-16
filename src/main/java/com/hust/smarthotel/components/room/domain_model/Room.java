package com.hust.smarthotel.components.room.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
public class Room {

    @Field("room_type")
    @Pattern(regexp = "^[A-Z0-9]{2,7}$")
    private String roomType;

    @NotBlank
    private String name;

    private String description;

    @Field("available_rooms")
    private Integer availableRooms;


    private List<Price> prices;
}
