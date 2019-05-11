package com.hust.smarthotel.components.user.domain_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.hust.smarthotel.generic.constant.RoleConstants.MANAGER;

@Data
@NoArgsConstructor
@Document(collection = "User")
public class User extends SysUser {

    @Id
    private String id;

    @Field("name")
    @Pattern(regexp = "^.{1,100}$")
    private String name;

    @Field("full_name")
    @Pattern(regexp = "^.{1,200}$")
    @JsonProperty(value = "full_name")
    private String fullName;

    @Field("email")
//    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    @Email
    @NotNull
    private String email;

    @Field("phone")
    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$")
    @NotNull
    private String phone;

    @Field("picture")
    private String picture;

    public User(Manager manager){
        this.setUsername(manager.getUsername());
        this.setPassword(manager.getPassword());
        this.setActive(true);
        this.setRole(MANAGER);
        this.name = manager.getName();
        this.fullName = manager.getFullName();
        this.email = manager.getEmail();
        this.phone = manager.getPhone();
    }

}
