package com.sacombank.db2demo.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sacombank.db2demo.entity.card.CardInformation;
import com.sacombank.db2demo.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardUserResponse {
    private User user;
    private CardInformation cardInformation;
}
