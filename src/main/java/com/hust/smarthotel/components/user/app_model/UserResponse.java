package com.hust.smarthotel.components.user.app_model;

import com.hust.smarthotel.components.user.domain_model.User;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserResponse {
    private Boolean status;
    private String message;
    private Integer code;
    private User user;
}
