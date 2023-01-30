package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.entity.CardInformation;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jdbc.JdbcConstants;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsertResponseProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            CardInfoRequest request = exchange.getProperty("REQUEST", CardInfoRequest.class);
            if (request.getCifId().equals("00000")) throw new Exception("InsertResponseProcess: exception thrown !!!!");

            List<Map<String, Object>> generatedKeys = exchange.getIn().getHeader(JdbcConstants.JDBC_GENERATED_KEYS_DATA, List.class);
            log.info("InsertResponseProcess -> JDBC_GENERATED_KEYS_DATA = {}", generatedKeys);
            Long idGenerated = (Long)generatedKeys.get(0).get(DBConstant.CardInformation.Column.ID);
            exchange.getIn().setBody(idGenerated);
        } catch (Exception ex) {
            log.error("InsertDBProcess failed: ", ex);
            throw ex;
        }
    }
}
