package com.hust.smarthotel.components.user.app_model;

import com.hust.smarthotel.components.user.domain_model.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerResponse {
    private Boolean status;
    private String message;
    private Integer code;
    private Manager manager;
}
