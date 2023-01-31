package com.sacombank.db2demo.processor;

import com.google.gson.Gson;
import com.sacombank.db2demo.constant.DBConstant;
import com.sacombank.db2demo.entity.CardInformation;
import com.sacombank.db2demo.model.ObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindByIdResponseProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            /*
            ObjectResponse response;
            List<CardInformation> body = (List<CardInformation>)exchange.getIn().getBody();
            response = CollectionUtils.isEmpty(body) ?
                    new ObjectResponse("Not Found", "001")
                    :
                    new ObjectResponse("Success", "000", body.get(0));

            exchange.getIn().setBody(response);
            */
            List<CardInformation> body = (List<CardInformation>)exchange.getIn().getBody();
            exchange.getIn().setBody(body.get(0));
        } catch (Exception ex) {
            log.error("FindByIdResponseProcess failed: ", ex);
        }
    }
}
