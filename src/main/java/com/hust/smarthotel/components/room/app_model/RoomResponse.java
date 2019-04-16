package com.hust.smarthotel.components.room.app_model;

import com.hust.smarthotel.components.room.domain_model.Rooms;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Boolean status;
    private String message;
    private Integer code;
    private Rooms rooms;
}
