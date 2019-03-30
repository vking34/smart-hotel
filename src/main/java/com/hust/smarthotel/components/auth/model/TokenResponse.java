package com.hust.smarthotel.components.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hust.smarthotel.components.user.domain_model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    @JsonProperty(value = "status")
    private Boolean status;

    @JsonProperty(value = "access_token")
    private String token;

    @JsonProperty(value = "user")
    private User user;

    public TokenResponse(Boolean status){
        this.status = status;
    }

}
