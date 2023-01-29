package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.Constant;
import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import com.sacombank.db2demo.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.sql.SqlConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsertSqlRequestProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            String body = exchange.getIn().getBody(String.class);

            CardInfoRequest cardInfoRequest = gson.fromJson(body, CardInfoRequest.class);

            String nowDateTimeStr = DateTimeUtils.getCurrentDateTime(Constant.PATTERN_TIMESTAMP);

            Map<String, Object> map = new HashMap<>();

            map.put(DBConstant.CardInformation.Column.CIF_ID, cardInfoRequest.getCifId());
            map.put(DBConstant.CardInformation.Column.CUST_NAME, cardInfoRequest.getCustName());
            map.put(DBConstant.CardInformation.Column.CARD_NUMBER, cardInfoRequest.getCardNumber());
            map.put(DBConstant.CardInformation.Column.CARD_TYPE, cardInfoRequest.getCardType());
            map.put(DBConstant.CardInformation.Column.UUID, UUID.randomUUID().toString());
            map.put(DBConstant.CardInformation.Column.CREATED_DATE, nowDateTimeStr);
            //map.put(DBConstant.CardInformation.Column.MODIFIED_DATE, nowDateTimeStr);

            exchange.getIn().setHeader(SqlConstants.SQL_RETRIEVE_GENERATED_KEYS, true);
            exchange.getIn().setHeader(DBConstant.CardInformation.Column.MODIFIED_DATE, nowDateTimeStr);
            exchange.getIn().setBody(map);

        } catch (Exception ex) {
            log.error("InsertSqlRequestProcess failed: ", ex);
        }
    }
}
