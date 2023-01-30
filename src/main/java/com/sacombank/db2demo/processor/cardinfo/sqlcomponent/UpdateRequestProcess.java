package com.sacombank.db2demo.processor.cardinfo.sqlcomponent;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.entity.CardInformation;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service("sqlUpdateRequestProcess")
@Slf4j
@RequiredArgsConstructor
public class UpdateRequestProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
//            String body = exchange.getIn().getBody(String.class);
//            CardInformation cardInformation = gson.fromJson(body, CardInformation.class);
//
//            CardInfoRequest request = exchange.getProperty("REQUEST", CardInfoRequest.class);
//
//            cardInformation.setCardType(request.getCardType());
//            cardInformation.setCustName(request.getCustName());
//            cardInformation.setCardNumber(request.getCardNumber());
//            cardInformation.setCifId(request.getCifId());
//            cardInformation.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
//
//            exchange.getIn().setBody(cardInfoRequest.getId());

        } catch (Exception ex) {
            log.error("SelectAllDBProcess failed: ", ex);
        }
    }
}
