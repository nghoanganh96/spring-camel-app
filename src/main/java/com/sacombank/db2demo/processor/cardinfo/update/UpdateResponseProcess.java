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
    public void process(Exchange exchange) throws Exception {
        try {
            CardInfoRequest request = exchange.getProperty("REQUEST", CardInfoRequest.class);
            if (request.getId().equals(222L)) throw new Exception("UpdateResponseProcess: Exception thrown");

        } catch (Exception ex) {
            log.error("UpdateResponseProcess failed: {}", ex.getMessage());
            throw ex;
        }
    }
}
