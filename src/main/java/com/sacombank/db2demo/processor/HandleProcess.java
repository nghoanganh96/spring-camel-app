package com.sacombank.db2demo.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HandleProcess implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            var testError = 1/0;
        } catch (Exception ex) {
            log.error("FindByIdRequestProcess failed: ", ex);
        }
    }
}
