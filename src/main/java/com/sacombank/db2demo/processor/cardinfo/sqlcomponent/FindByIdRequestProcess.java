package com.sacombank.db2demo.processor.cardinfo.sqlcomponent;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("sqlFindByIdRequestProcess")
@Slf4j
@RequiredArgsConstructor
public class FindByIdRequestProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            String body = exchange.getIn().getBody(String.class);
            Long id = Long.parseLong(body);

            Map<String, Object> map = new HashMap<>();
            map.put("TABLE", DBConstant.CardInformation.NAME);
            map.put("COLUMN", DBConstant.CardInformation.Column.ID);
            map.put("VALUE", id);

            exchange.getIn().setBody(map);

        } catch (Exception ex) {
            log.error("FindByIdRequestProcess failed: ", ex);
        }
    }
}
