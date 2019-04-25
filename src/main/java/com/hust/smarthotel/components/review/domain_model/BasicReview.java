package com.hust.smarthotel.components.review.domain_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicReview {

    @NotNull
    private RUser user;

    @Field("rating_point")
    @JsonProperty("rating_point")
    @NotNull
    private Integer ratingPoint;

    @Size(max = 400)
    private String comment;

    public BasicReview(BasicReview basicReview){
        this.user = basicReview.user;
        this.ratingPoint = basicReview.ratingPoint;
        this.comment = basicReview.comment;
    }
}
