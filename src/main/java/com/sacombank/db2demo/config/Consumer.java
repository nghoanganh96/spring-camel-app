package com.sacombank.db2demo.config;

import com.sacombank.db2demo.config.activemq.ActiveMQProcess;
import com.sacombank.db2demo.constant.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;

import javax.jms.JMSException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
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
                try {
                    log.info("message type: {}", message.getJMSType());
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
                if (message instanceof ActiveMQTextMessage textMessage) {
                    activeMQProcess.executeMessage(textMessage);
                } else {
                    log.warn("Cannot parse message. it not a ActiveMQ message");
                }
            });
        });

        registrar.registerEndpoint(endpoint);
    }
}
