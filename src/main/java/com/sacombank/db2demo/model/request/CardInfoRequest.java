package com.sacombank.db2demo.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfoRequest {

    private Long id;
    private String cifId;

    private String cardNumber;

    private String custName;

    private String cardType;
}
