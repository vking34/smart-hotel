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
public class Review {

    @NotNull
    private User user;


    @Field("rating_point")
    @JsonProperty("rating_point")
    private Integer ratingPoint;

    @Size(max = 400)
    private String comment;

    @Field("created_time")
    private LocalDateTime createdTime;
}
