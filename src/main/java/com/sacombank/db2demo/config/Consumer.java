package com.sacombank.db2demo.config;

import com.sacombank.db2demo.config.activemq.ActiveMQProcess;
import com.sacombank.db2demo.constant.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class Consumer implements JmsListenerConfigurer {

    @Value("${activemq-jds.name}")
    private String activeMQName;

    private final ActiveMQProcess activeMQProcess;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(String.format("%s.%s", activeMQName, Constant.QUEUE_NAME_REQUEST));
        endpoint.setDestination(Constant.QUEUE_NAME_REQUEST);
        endpoint.setMessageListener(message -> {
            CompletableFuture.runAsync(() -> {
                if (message instanceof ActiveMQTextMessage textMessage) {
                    activeMQProcess.executeMessage(textMessage);
                } else {
                    log.warn("Cannot parse message. it's not an ActiveMQ message");
                }
            });
        });

        registrar.registerEndpoint(endpoint);
    }
}
