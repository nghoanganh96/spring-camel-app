package com.sacombank.db2demo.processor.cardinfo.update;

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

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateRequestProcess implements Processor {


    @Override
    public void process(Exchange exchange) {
        try {
            CardInformation cardInformation = exchange.getIn().getBody(CardInformation.class);
            CardInfoRequest request = exchange.getProperty("REQUEST", CardInfoRequest.class);

            StringBuilder valueUpdates = new StringBuilder("");
            if (!request.getCardType().equalsIgnoreCase(cardInformation.getCardType())) {
                valueUpdates.append(DBConstant.CardInformation.Column.CARD_TYPE).append("=").append(String.format("'%s'", request.getCardType())).append(",");
            }
            if (!request.getCustName().equalsIgnoreCase(cardInformation.getCustName())) {
                valueUpdates.append(DBConstant.CardInformation.Column.CUST_NAME).append("=").append(String.format("'%s'", request.getCustName())).append(",");
            }
            if (!request.getCardNumber().equalsIgnoreCase(cardInformation.getCardNumber())) {
                valueUpdates.append(DBConstant.CardInformation.Column.CARD_NUMBER).append("=").append(String.format("'%s'", request.getCardNumber())).append(",");
            }
            if (!request.getCifId().equalsIgnoreCase(cardInformation.getCifId())) {
                valueUpdates.append(DBConstant.CardInformation.Column.CIF_ID).append("=").append(String.format("'%s'", request.getCifId())).append(",");
            }
            valueUpdates.append(DBConstant.CardInformation.Column.MODIFIED_DATE).append("=").append(String.format("'%s'", LocalDateTime.now()));

            if (!valueUpdates.isEmpty()) {

                String where = String.format("%s = %s", DBConstant.CardInformation.Column.ID, cardInformation.getId());
                String sqlUpdate = String.format("UPDATE %s SET %s WHERE %s", DBConstant.CardInformation.NAME, valueUpdates, where);

                log.info("Sql Update: {}", sqlUpdate);

                exchange.getIn().setBody(sqlUpdate);
            }
        } catch (Exception ex) {
            log.error("UpdateRequestProcess failed: ", ex);
        }
    }
}
