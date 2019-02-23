package com.hust.smarthotel.components.room.domain_model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Price {
    private String type;
    private Integer price;
}
