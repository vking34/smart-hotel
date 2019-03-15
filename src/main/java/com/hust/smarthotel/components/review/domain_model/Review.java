package com.hust.smarthotel.components.review.domain_model;


import com.hust.smarthotel.components.user.domain_model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
class Review {

    @NotNull
    private User user;

    @Size(max = 5)
    private Integer ratingPoint;

    @Size(max = 400)
    private String comment;
}
