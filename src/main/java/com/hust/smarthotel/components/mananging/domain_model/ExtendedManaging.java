package com.hust.smarthotel.components.mananging.domain_model;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedManaging extends Managing {

    @Field("hotel")
    private Hotel hotel;
}
