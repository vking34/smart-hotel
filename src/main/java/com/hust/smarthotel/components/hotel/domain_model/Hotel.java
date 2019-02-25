package com.hust.smarthotel.components.hotel.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "Hotel")
@AllArgsConstructor
public class Hotel {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("address")
    private String address;

    @Field("description")
    private String description;

    @Field("point")
    private Float point;

    @Field("ratings")
    private Integer ratings;

    @Field("location")
    private Location location;

    @Field("facilities")
    private Facilities facilities;

    @Field("status")
    private Boolean status;

    @Field("active")
    private Boolean active;

}
