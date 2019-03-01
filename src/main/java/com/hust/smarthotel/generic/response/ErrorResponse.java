package com.hust.smarthotel.generic.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private Error error;
    public ErrorResponse(String msg, String type, Integer code){
        error = new Error(msg, type, code);
    }


}
