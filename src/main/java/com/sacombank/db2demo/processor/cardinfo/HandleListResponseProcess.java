package com.sacombank.db2demo.processor.cardinfo;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.entity.CardInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sqlHandleListResponseProcess")
@Slf4j
@RequiredArgsConstructor
public class HandleListResponseProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            ArrayList<Map<String, Object>> response = (ArrayList<Map<String, Object>>)exchange.getIn().getBody();
            List<CardInformation> cardInformationList = new ArrayList<>();

            response.forEach(data -> {
                var cardInfoResponse =  new CardInformation();
                cardInfoResponse.setId((Long)data.get(DBConstant.CardInformation.Column.ID));
                cardInfoResponse.setCifId((String)data.get(DBConstant.CardInformation.Column.CIF_ID));
                cardInfoResponse.setCustName((String)data.get(DBConstant.CardInformation.Column.CUST_NAME));
                cardInfoResponse.setCardNumber((String)data.get(DBConstant.CardInformation.Column.CARD_NUMBER));
                cardInfoResponse.setCardType((String)data.get(DBConstant.CardInformation.Column.CARD_TYPE));
                cardInfoResponse.setUuid((String)data.get(DBConstant.CardInformation.Column.UUID));
                cardInfoResponse.setCreatedDate((Timestamp)data.get(DBConstant.CardInformation.Column.CREATED_DATE));
                cardInfoResponse.setModifiedDate((Timestamp)data.get(DBConstant.CardInformation.Column.MODIFIED_DATE));

                cardInformationList.add(cardInfoResponse);
            });

            exchange.getIn().setBody(cardInformationList);
        } catch (Exception ex) {
            log.error("SelectAllDBProcess failed: ", ex);
        }
    }
}
