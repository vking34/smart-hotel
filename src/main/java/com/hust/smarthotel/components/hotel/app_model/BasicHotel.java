package com.hust.smarthotel.components.hotel.app_model;

import com.hust.smarthotel.components.hotel.domain_model.Facilities;
import com.hust.smarthotel.components.hotel.domain_model.Location;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
public class BasicHotel {

    @ApiModelProperty(notes = "Hotel Name", required = true)
    @Field("name")
    private String name;

    @ApiModelProperty(notes = "Hotel Address", required = true)
    @Field("address")
    private String address;

    @ApiModelProperty(notes = "Description about hotel")
    @Field("description")
    private String description;

    @ApiModelProperty(notes = "Location of hotel", required = true)
    @Field("location")
    private Location location;

    @ApiModelProperty(notes = "Facilities like wifi, bar, laundry,...", required = true)
    @Field("facilities")
    private Facilities facilities;

    @ApiModelProperty("Avatar/Logo")
    @Field("logo")
    private String logo;

    @ApiModelProperty(notes = "Photos about hotels")
    @Field("photos")
    private List<String> photos;

    @ApiModelProperty(notes = "Active or not?", required = true)
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
