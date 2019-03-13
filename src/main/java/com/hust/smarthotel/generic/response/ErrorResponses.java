package com.hust.smarthotel.generic.response;

import com.hust.smarthotel.generic.model.ErrorResponse;

public class ErrorResponses {
    public static final ErrorResponse NOT_FOUND = new ErrorResponse("Not found entity by id", "NOTFOUND", 100);
}
