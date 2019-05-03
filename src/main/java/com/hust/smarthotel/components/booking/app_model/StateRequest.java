package com.hust.smarthotel.components.booking.app_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateRequest {

    @Pattern(regexp = "^(CANCELED|DENIED|ACCEPTED|CHANGED_ACCEPTED)$")
    @NotNull
    private String status;

    private Integer price;
}
