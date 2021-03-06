package com.hust.smarthotel.components.review.domain_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
class RUser {

    @NotNull
    @Pattern(regexp = "^[a-z0-9]{24}$")
    private String id;

    private String name;

    @JsonProperty("full_name")
    @Field("full_name")
    private String fullName;

    private String picture;
}
