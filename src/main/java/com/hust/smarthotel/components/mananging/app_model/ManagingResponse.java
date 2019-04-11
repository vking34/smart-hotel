package com.hust.smarthotel.components.mananging.app_model;

import com.hust.smarthotel.components.mananging.domain_model.Managing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagingResponse {
    private Boolean status;
    private String message;
    private Integer code;
    private Managing managing;
}
