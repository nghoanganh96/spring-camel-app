package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import com.sacombank.db2demo.entity.CardInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SelectAllResponseProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            ArrayList<HashMap<String, Object>> response = (ArrayList<HashMap<String, Object>>)exchange.getIn().getBody();
            List<CardInformation> cardInformationList = new ArrayList<>();

            response.forEach(data -> {
                var cardInfoResponse =  new CardInformation();
                cardInfoResponse.setId((Long)data.get("ID"));
                cardInfoResponse.setCifId((String)data.get("CIF_ID"));
                cardInfoResponse.setCustName((String)data.get("CUST_NAME"));
                cardInfoResponse.setCardNumber((String)data.get("CARD_NUMBER"));
                cardInfoResponse.setCardType((String)data.get("CARD_TYPE"));
                cardInfoResponse.setUuid((String)data.get("UUID"));
                cardInfoResponse.setCreatedDate((Timestamp)data.get("CREATED_DATE"));
                cardInfoResponse.setModifiedDate((Timestamp)data.get("MODIFIED_DATE"));

                cardInformationList.add(cardInfoResponse);
            });

            exchange.getIn().setBody(cardInformationList);
        } catch (Exception ex) {
            log.error("SelectAllDBProcess failed: ", ex);
        }
    }
}
