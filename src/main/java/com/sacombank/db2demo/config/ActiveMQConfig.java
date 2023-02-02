package com.sacombank.db2demo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Component;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

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

        return connectionFactory;
    }

    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(this.activeMQConnectionFactory());
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        cachingConnectionFactory.setReconnectOnException(true);

        return cachingConnectionFactory;
    }

    @Bean(name = "activemqComponent")
    public Component activeMQComponent(@Qualifier("jmsTransactionManagerMsg") PlatformTransactionManager transactionManager) {
        var jmsComponent = JmsComponent.jmsComponentAutoAcknowledge(this.connectionFactory());
        jmsComponent.setTransacted(true);
        jmsComponent.setTransactionManager(transactionManager);
        return jmsComponent;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(this.connectionFactory());
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

    // Create Transaction Manager
    @Bean("jmsTransactionManagerMsg")
    public PlatformTransactionManager transactionManager(@Qualifier("activemq-db2") ActiveMQConnectionFactory activeMQConnectionFactory) {
        return new JmsTransactionManager(activeMQConnectionFactory);
    }

    // Specify Spring Transaction Management Policy
    @Bean(name = "txRequired")
    public SpringTransactionPolicy propagationRequired(@Qualifier("jmsTransactionManagerMsg") PlatformTransactionManager transactionManager) {
        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
        propagationRequired.setTransactionManager(transactionManager);
        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return propagationRequired;
    }
//
//    @Bean(name = "TX_REQUIRES_NEW")
//    public SpringTransactionPolicy propagationRequiresNew(PlatformTransactionManager transactionManager) {
//        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
//        propagationRequired.setTransactionManager(transactionManager);
//        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
//        return propagationRequired;
//    }
//
//    @Bean(name = "TX_REQUIRES_MANDATORY")
//    public SpringTransactionPolicy propagationRequiresMandatory(PlatformTransactionManager transactionManager) {
//        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
//        propagationRequired.setTransactionManager(transactionManager);
//        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRES_MANDATORY");
//        return propagationRequired;
//    }

}
