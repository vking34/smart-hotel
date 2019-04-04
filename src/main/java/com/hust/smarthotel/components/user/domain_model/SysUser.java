package com.hust.smarthotel.components.user.domain_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@NoArgsConstructor
public class SysUser {

    @Pattern(regexp = "^(?=.{8,100}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")
                                                                        // no _ or . at the end
                                                          // allowed characters a-zA-Z0-9._
                                             // no __ or _. or ._ or .. inside
                                      // no _ or . at the beginning
                        // 8-100 chars long
    @Field("username")
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
