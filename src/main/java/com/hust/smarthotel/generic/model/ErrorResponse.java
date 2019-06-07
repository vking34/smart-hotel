package com.hust.smarthotel.generic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private Boolean status;
    private String message;
    private Integer code;
}
