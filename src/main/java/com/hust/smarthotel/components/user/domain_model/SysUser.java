package com.hust.smarthotel.components.user.domain_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
class SysUser {

    @Field("username")
    @Size(min = 6, max = 100)
    private String username;

    @Field("password")
//    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @Field("active")
    @JsonIgnore
    private Boolean active;

    @Field("role")
//    @JsonIgnore
    private String role;
}
