package com.sacombank.db2demo.processor.cardinfo.jpacomponent;

import com.google.gson.Gson;
import com.sacombank.db2demo.entity.CardInformation;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaveJpaResponseProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            CardInformation body = exchange.getIn().getBody(CardInformation.class);
            log.info("SaveJpaResponseProcess: body: {}", body);

        } catch (Exception ex) {
            log.error("SaveJpaRequestProcess failed: ", ex);
        }
    }
}
