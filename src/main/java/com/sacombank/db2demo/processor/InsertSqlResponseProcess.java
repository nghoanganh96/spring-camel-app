package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.entity.CardInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jdbc.JdbcConstants;
import org.apache.camel.component.sql.SqlConstants;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsertSqlResponseProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            List<Map<String, Object>> generatedKeys = exchange.getIn().getHeader(SqlConstants.SQL_GENERATED_KEYS_DATA, List.class);
            log.info("InsertSqlResponseProcess -> SQL_GENERATED_KEYS_DATA = {}", generatedKeys);
            Long idGenerated = (Long)generatedKeys.get(0).get(DBConstant.CardInformation.Column.ID);
            exchange.getIn().setBody(CardInformation.builder().id(idGenerated).build());
        } catch (Exception ex) {
            log.error("InsertSqlResponseProcess failed: ", ex);
        }
    }
}
