package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsertRequestProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            String body = exchange.getIn().getBody(String.class);

            CardInfoRequest cardInfoRequest = gson.fromJson(body, CardInfoRequest.class);

            String query = String.format("INSERT INTO CARD_INFORMATION(CIF_ID, CUST_NAME, CARD_NUMBER, CARD_TYPE, UUID, CREATED_DATE, MODIFIED_DATE) VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s')"
                    , cardInfoRequest.getCifId()
                    , cardInfoRequest.getCustName()
                    , cardInfoRequest.getCardNumber()
                    , cardInfoRequest.getCardType()
                    , UUID.randomUUID()
                    , LocalDateTime.now()
                    , LocalDateTime.now()
            );
            exchange.getIn().setBody(query);
        } catch (Exception ex) {
            log.error("InsertDBProcess failed: ", ex);
        }
    }
}
