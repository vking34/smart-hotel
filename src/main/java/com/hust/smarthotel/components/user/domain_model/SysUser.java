package com.hust.smarthotel.components.user.domain_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class SysUser {

    @Field("username")
    
    private String username;

    @Field("password")
    @JsonIgnore
    private String password;

    @Field("active")
    @JsonIgnore
    private Boolean active;

    @Field("role")
//    @JsonIgnore
    private String role;
}
