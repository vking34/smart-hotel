package com.hust.smarthotel.components.hotel.app_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerStatus {

    @Pattern(regexp = "^(verified|rejected)$")
    @NotNull
    private String status;
}
