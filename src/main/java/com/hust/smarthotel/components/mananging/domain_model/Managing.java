package com.hust.smarthotel.components.mananging.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Managing")
public class Managing {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("hotel_id")
    private String hotelId;

    public Managing(String userId, String hotelId) {
        this.userId = userId;
        this.hotelId = hotelId;
    }
}
