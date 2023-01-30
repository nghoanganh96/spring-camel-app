package com.sacombank.db2demo.processor.cardinfo.delete;

import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.entity.CardInformation;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteRequestProcess implements Processor {


    @Override
    public void process(Exchange exchange) {
        try {
            CardInformation cardInformation = exchange.getIn().getBody(CardInformation.class);

            if (null != cardInformation) {

                String where = String.format("%s = %s", DBConstant.CardInformation.Column.ID, cardInformation.getId());
                String sqlDelete = String.format("DELETE FROM %s WHERE %s", DBConstant.CardInformation.NAME, where);

                log.info("Sql Delete: {}", sqlDelete);

                exchange.getIn().setBody(sqlDelete);
            }
        } catch (Exception ex) {
            log.error("DeleteRequestProcess failed: ", ex);
        }
    }
}
