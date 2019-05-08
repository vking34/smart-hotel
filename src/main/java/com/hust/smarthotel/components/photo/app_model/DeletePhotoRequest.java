package com.hust.smarthotel.components.photo.app_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletePhotoRequest {

    @Pattern(regexp = "^(logo|photo|all)$")
    @NotNull
    private String type;


    @Min(0)
    @Max(4)
    private Integer position;
}
