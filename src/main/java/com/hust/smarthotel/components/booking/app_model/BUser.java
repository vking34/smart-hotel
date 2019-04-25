package com.hust.smarthotel.components.booking.app_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BUser {

    @Pattern(regexp = "^[a-z0-9]{24}$")
    private String id;

    @NotBlank
    private String name;

    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$")
    @NotNull
    private String phone;

    private String email;

    @JsonProperty("full_name")
    @Field("full_name")
    private String fullName;

    private String picture;
}
