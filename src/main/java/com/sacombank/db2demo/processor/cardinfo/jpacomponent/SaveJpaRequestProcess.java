package com.sacombank.db2demo.processor.cardinfo.jpacomponent;

import com.google.gson.Gson;
import com.sacombank.db2demo.entity.CardInformation;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jpa.JpaConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaveJpaRequestProcess implements Processor {

    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        try {
            String body = exchange.getIn().getBody(String.class);
            CardInfoRequest request = gson.fromJson(body, CardInfoRequest.class);

            CardInformation entity = CardInformation.builder()
                    .cifId(request.getCifId())
                    .custName(request.getCustName())
                    .cardNumber(request.getCardNumber())
                    .cardType(request.getCardType())
                    .uuid(UUID.randomUUID().toString())
                    .build();

            exchange.getIn().setBody(entity);

        } catch (Exception ex) {
            log.error("SaveJpaRequestProcess failed: ", ex);
        }
    }
}
