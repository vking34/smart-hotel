package com.hust.smarthotel.components.user.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager extends User {

    @Field("hotel_id")
    @Size(max = 25)
    private String hotelId;
}
