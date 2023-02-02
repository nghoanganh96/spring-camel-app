package com.sacombank.db2demo.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCardInfoRequest {
    private String cifId;
    private String moreInfo;
    private String cardNumber;
    private String cardType;
    private String custName;
}
