package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindByIdRequestProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            String body = exchange.getIn().getBody(String.class);
            Long id = Long.parseLong(body);
            String query = String.format("SELECT * FROM %s WHERE %s = %s", DBConstant.CardInformation.NAME, DBConstant.CardInformation.Column.ID, id);
            exchange.getIn().setBody(query);
        } catch (Exception ex) {
            log.error("FindByIdRequestProcess failed: ", ex);
        }
    }
}
