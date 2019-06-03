package com.hust.smarthotel.components.hotel.domain_model;

import com.hust.smarthotel.components.hotel.app_model.BasicHotel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "Hotel")
@NoArgsConstructor
public class Hotel extends BasicHotel {
    @Id
    private String id;

    @Field("point")
    private Double point;

    @Field("ratings")
    private Integer ratings;

    @Field("active")
    private Boolean active;

    @Field("logo")
    private String logo;

    @Field("photos")
    private List<String> photos;

    public Hotel(BasicHotel basicHotel) {
        super(basicHotel);
        this.point = (double) 0;
        this.ratings = 0;
        this.active = true;
        this.logo = "";
        this.photos = new ArrayList<String>();
    }
}
