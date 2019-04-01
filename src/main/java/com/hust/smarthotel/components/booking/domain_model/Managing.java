package com.hust.smarthotel.components.booking.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Managing")
public class Managing {

    @Field("hotel_id")
    private String hotelId;

    @Field("user_id")
    private String userId;

}
