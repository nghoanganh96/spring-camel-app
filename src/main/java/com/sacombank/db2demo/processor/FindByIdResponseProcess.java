package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.entity.CardInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindByIdResponseProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            List<CardInformation> body = (List<CardInformation>)exchange.getIn().getBody();
            exchange.getIn().setBody(body.get(0));
        } catch (Exception ex) {
            log.error("FindByIdResponseProcess failed: ", ex);
        }
    }
}
