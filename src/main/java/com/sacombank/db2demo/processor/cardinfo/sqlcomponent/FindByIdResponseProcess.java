package com.sacombank.db2demo.processor.cardinfo.sqlcomponent;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.entity.CardInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sqlFindByIdResponseProcess")
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
