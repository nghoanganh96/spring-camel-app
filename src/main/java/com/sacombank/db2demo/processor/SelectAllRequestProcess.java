package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SelectAllRequestProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            String query = "SELECT * FROM CARD_INFORMATION";
            exchange.getIn().setBody(query);
        } catch (Exception ex) {
            log.error("SelectAllDBProcess failed: ", ex);
        }
    }
}
