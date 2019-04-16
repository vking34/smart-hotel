package com.hust.smarthotel.components.room.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class Price {

    @Pattern(regexp = "^[A-Z0-9]{2,5}$")
    private String type;

    private String name;

    private Integer price;
}
