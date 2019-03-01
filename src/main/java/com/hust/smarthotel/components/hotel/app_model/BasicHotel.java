package com.hust.smarthotel.components.hotel.app_model;

import com.hust.smarthotel.components.hotel.domain_model.Facilities;
import com.hust.smarthotel.components.hotel.domain_model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class BasicHotel {

    @Field("name")
    private String name;

    @Field("address")
    private String address;

    @Field("description")
    private String description;

    @Field("location")
    private Location location;

    @Field("facilities")
    private Facilities facilities;

    @Field("status")
    private Boolean status;



    public BasicHotel(BasicHotel hotel){
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.description = hotel.getDescription();
        this.location = hotel.getLocation();
        this.facilities = hotel.getFacilities();
        this.status = hotel.getStatus();
    }

}
