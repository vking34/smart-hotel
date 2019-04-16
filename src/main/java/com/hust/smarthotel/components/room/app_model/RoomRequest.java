package com.hust.smarthotel.components.room.app_model;

import com.hust.smarthotel.components.room.domain_model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @NotNull
    private List<Room> rooms;

}
