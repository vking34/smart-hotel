package com.hust.smarthotel.components.user.domain_model;

import com.hust.smarthotel.generic.util.EncryptedPasswordUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

import static com.hust.smarthotel.generic.constant.ManagerState.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "VerifyingManager")
public class Manager extends BasicManager {
    @Id
    private String id;

    @Field("created_time")
    private LocalDateTime createdTime;

    @Field("status")
    private String status;

    @Field("reason")
    private String reason;

    public Manager(BasicManager basicManager){
        this.setUsername(basicManager.getUsername());
        this.setPassword(EncryptedPasswordUtils.encryptPassword(basicManager.getPassword()));
        this.setName(basicManager.getName());
        this.setFullName(basicManager.getFullName());
        this.setEmail(basicManager.getEmail());
        this.setPhone(basicManager.getPhone());
        this.createdTime = LocalDateTime.now();
        this.status = VERIFYING;
    }

    public void reject(){
        this.status = REJECTED;
    }

    public void reject(String reason){
        this.status = REJECTED;
        this.reason = reason;
    }

    public void verify(){
        this.status = VERIFIED;
    }
}
