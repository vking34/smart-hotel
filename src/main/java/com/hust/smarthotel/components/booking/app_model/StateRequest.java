package com.hust.smarthotel.components.booking.app_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateRequest {

    @Pattern(regexp = "^(CANCELED|ACCEPTED)$")
    private String status;
}
