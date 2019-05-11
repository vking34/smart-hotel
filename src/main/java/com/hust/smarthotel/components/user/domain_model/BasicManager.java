package com.hust.smarthotel.components.user.domain_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicManager {

    @Pattern(regexp = "^(?=.{8,100}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")
    // no _ or . at the end
    // allowed characters a-zA-Z0-9._
    // no __ or _. or ._ or .. inside
    // no _ or . at the beginning
    // 8-100 chars long
    @Field("username")
    @NotNull
    private String username;

    @Field("password")
//    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @Field("name")
    @Pattern(regexp = "^.{1,100}$")
    private String name;

    @Field("full_name")
    @Pattern(regexp = "^.{1,200}$")
    @JsonProperty(value = "full_name")
    @NotNull
    private String fullName;

    @Field("email")
    @Email
    @NotNull
    private String email;

    @Field("phone")
    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$")
    @NotNull
    private String phone;

}
