package com.sacombank.db2demo.processor.cardinfo.sqlcomponent;

import com.google.gson.Gson;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service("sqlPreUpdateRequestProcess")
@Slf4j
@RequiredArgsConstructor
public class PreUpdateRequestProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            String body = exchange.getIn().getBody(String.class);
            CardInfoRequest cardInfoRequest = gson.fromJson(body, CardInfoRequest.class);

            exchange.setProperty("REQUEST", cardInfoRequest);
            exchange.getIn().setBody(cardInfoRequest.getId());

        } catch (Exception ex) {
            log.error("PreUpdateRequestProcess failed: ", ex);
        }
    }
}
