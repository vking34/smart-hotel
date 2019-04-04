package com.hust.smarthotel.components.review.domain_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    private String role;
    private String name;

    @JsonProperty("full_name")
    @Field("full_name")
    private String fullName;

    private String picture;
}
