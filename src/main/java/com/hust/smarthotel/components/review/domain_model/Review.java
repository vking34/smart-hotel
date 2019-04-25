package com.hust.smarthotel.components.review.domain_model;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BasicReview {

    @Field("created_time")
    private LocalDateTime createdTime;

    public Review(BasicReview basicReview){
        super(basicReview);
        this.createdTime = LocalDateTime.now();
    }
}
