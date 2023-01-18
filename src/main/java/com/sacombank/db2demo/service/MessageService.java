package com.sacombank.db2demo.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final JmsTemplate jmsTemplate;
    private final Gson gson;

    public void sendMessageToQueue(String queueName, Object data) {
        try {
            log.info("Send data to queue {}: {}", queueName, gson.toJson(data));
            jmsTemplate.convertAndSend(queueName, gson.toJson(data));
        } catch (Exception ex) {
            log.error("Cannot send message to queue {}: ", queueName, ex);
        }
    }
}
