package com.hust.smarthotel.components.photo.app_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponse {
    private Boolean status;
    private String message;
    private Integer code;
    private String url;
}
