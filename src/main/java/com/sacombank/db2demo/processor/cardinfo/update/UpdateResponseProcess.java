package com.sacombank.db2demo.processor.cardinfo.update;

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
public class UpdateResponseProcess implements Processor {


    @Override
    public void process(Exchange exchange) {
        try {
            String body = exchange.getIn().getBody(String.class);
            exchange.getIn().setBody(body);

        } catch (Exception ex) {
            log.error("UpdateResponseProcess failed: ", ex);
        }
    }
}
