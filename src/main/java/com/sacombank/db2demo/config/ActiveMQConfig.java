package com.sacombank.db2demo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.camel.Component;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${activemq-jds.host}")
    private String host;

    @Value("${activemq-jds.session-cache-size}")
    private int sessionCacheSize;

    @Bean(name = "activemq-db2")
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(host);
        connectionFactory.setUseAsyncSend(true);
        connectionFactory.setTrustAllPackages(true);
        connectionFactory.setDispatchAsync(false);
        connectionFactory.setOptimizeAcknowledge(true);
        connectionFactory.setAlwaysSessionAsync(true);

        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(1);
        redeliveryPolicy.setRedeliveryDelay(3000);

        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);

        return connectionFactory;
    }

    @Bean(name = "cacheActiveMQConnectionFactory")
    public ConnectionFactory connectionFactory(@Qualifier("activemq-db2") ActiveMQConnectionFactory activeMQConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        cachingConnectionFactory.setReconnectOnException(true);

        return cachingConnectionFactory;
    }

    @Bean(name = "activemqComponent")
    public Component activeMQComponent(/*@Qualifier("jmsTransactionManagerMsg") PlatformTransactionManager transactionManager
            , */@Qualifier("cacheActiveMQConnectionFactory") ConnectionFactory connectionFactory) {

        var jmsComponent = JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
//        jmsComponent.setTransacted(true);
//        jmsComponent.setTransactionManager(transactionManager);
        return jmsComponent;
    }

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier("cacheActiveMQConnectionFactory") ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDeliveryPersistent(false);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(this.activeMQConnectionFactory());

        // true: using jms topic, false: using jms queue
        // factory.setPubSubDomain(false);
        return factory;
    }

}
