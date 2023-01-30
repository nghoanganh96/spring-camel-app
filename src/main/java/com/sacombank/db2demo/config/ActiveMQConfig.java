package com.sacombank.db2demo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Component;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
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

    // Create Transaction Manager
    @Bean
    public PlatformTransactionManager transactionManager(ActiveMQConnectionFactory activeMQConnectionFactory) {
        return new JmsTransactionManager(activeMQConnectionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManagerDatasource(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // Specify Spring Transaction Management Policy
    @Bean(name = "TX_REQUIRED")
    public SpringTransactionPolicy propagationRequired(PlatformTransactionManager transactionManagerDatasource) {
        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
        propagationRequired.setTransactionManager(transactionManagerDatasource);
        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return propagationRequired;
    }

    @Bean(name = "TX_REQUIRES_NEW")
    public SpringTransactionPolicy propagationRequiresNew(PlatformTransactionManager transactionManagerDatasource) {
        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
        propagationRequired.setTransactionManager(transactionManagerDatasource);
        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
        return propagationRequired;
    }

    @Bean(name = "TX_REQUIRES_MANDATORY")
    public SpringTransactionPolicy propagationRequiresMandatory(PlatformTransactionManager transactionManagerDatasource) {
        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
        propagationRequired.setTransactionManager(transactionManagerDatasource);
        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRES_MANDATORY");
        return propagationRequired;
    }

    @Bean(name = "activemqComponent")
    public Component activeMQComponent(PlatformTransactionManager transactionManager, ActiveMQConnectionFactory activeMQConnectionFactory) {
        var jmsComponent = JmsComponent.jmsComponent(activeMQConnectionFactory);
        jmsComponent.setTransacted(true);
        jmsComponent.setTransactionManager(transactionManager);
//        jmsComponent.setCacheLevelName("CACHE_CONSUMER");
        return jmsComponent;
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory activeMQConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory);
        jmsTemplate.setDeliveryPersistent(false);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ActiveMQConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory);

        // true: using jms topic, false: using jms queue
        // factory.setPubSubDomain(false);
        return factory;
    }


}
