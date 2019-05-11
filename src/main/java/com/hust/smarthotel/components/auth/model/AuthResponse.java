package com.hust.smarthotel.components.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @JsonProperty(value = "status")
    private Boolean status;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "code")
    private Integer code;
}
