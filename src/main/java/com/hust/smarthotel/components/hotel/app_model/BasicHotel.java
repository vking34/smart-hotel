package com.hust.smarthotel.components.hotel.app_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hust.smarthotel.components.hotel.domain_model.Facilities;
import com.hust.smarthotel.components.hotel.domain_model.Location;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class BasicHotel {

    @ApiModelProperty(notes = "Hotel Name", required = true)
    @Field("name")
    @NotNull
    @Size(min = 1, max = 200)
    private String name;

    @ApiModelProperty(notes = "Hotel Address", required = true)
    @Field("address")
    @NotNull
    @Size(min = 1, max = 300)
    private String address;

    @ApiModelProperty(notes = "Description about hotel")
    @Field("description")
    @NotNull
    @Size(min = 1, max = 500)
    private String description;

    @ApiModelProperty(notes = "Hotline", required = true)
    @Field("phone_number")
    @NotNull
    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$")
    private String phoneNumber;

    @ApiModelProperty(notes = "Location of hotel", required = true)
    @Field("location")
    private Location location;

    @ApiModelProperty(notes = "Facilities like wifi, bar, laundry,...", required = true)
    @Field("facilities")
    private Facilities facilities;

    @ApiModelProperty(notes = "Active or not?", required = true)
    @Field("status")
//    @NotNull
    private Boolean status;

    @Field("average_price")
    @Positive
    private Integer averagePrice;

    public BasicHotel(BasicHotel hotel){
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.description = hotel.getDescription();
        this.phoneNumber = hotel.getPhoneNumber();
        this.location = hotel.getLocation();
        this.facilities = hotel.getFacilities();
        this.averagePrice = hotel.getAveragePrice();
        if (hotel.getStatus() == null)
            this.status = true;
        else
            this.status = hotel.getStatus();

//        if (hotel.getAveragePrice() == null)
//            this.averagePrice = 0;
//        else
//            this.averagePrice = hotel.getAveragePrice();

    }
}
