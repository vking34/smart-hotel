package com.hust.smarthotel.components.user.domain_model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@Document(collection = "User")
public class User extends SysUser {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("full_name")
    private String fullName;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("picture")
    private String picture;
}
