package com.hust.smarthotel.generic.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Error {
    private String message;
    private String type;
    private Integer code;
}
