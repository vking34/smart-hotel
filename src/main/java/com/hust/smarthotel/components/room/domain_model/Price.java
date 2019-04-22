package com.hust.smarthotel.components.room.domain_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class Price {
    private String name;

//    @Pattern(regexp = "^[A-Z0-9]{2,5}$")
    @JsonProperty("rent_type")
    @Field("rent_type")
    private Integer rentType;

    @JsonProperty("rent_value")
    @Field("rent_value")
    private Integer rentValue;

    @JsonProperty("start_time")
    @Field("start_time")
    private Integer startTime;

    @JsonProperty("end_time")
    @Field("end_time")
    private Integer endTime;

    private Integer price;
}
