package com.sacombank.db2demo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Component;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
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

    @Bean(name = "cacheActiveMQConnectionFactory")
    public ConnectionFactory connectionFactory(@Qualifier("activemq-db2") ActiveMQConnectionFactory activeMQConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        cachingConnectionFactory.setReconnectOnException(true);

        return cachingConnectionFactory;
    }

    @Bean(name = "activemqComponent")
    public Component activeMQComponent(@Qualifier("jmsTransactionManagerMsg") PlatformTransactionManager transactionManager
            , @Qualifier("cacheActiveMQConnectionFactory") ConnectionFactory connectionFactory) {

        var jmsComponent = JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
        jmsComponent.setTransacted(true);
        jmsComponent.setTransactionManager(transactionManager);
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

    // Create Transaction Manager
    @Bean("jmsTransactionManagerMsg")
    public PlatformTransactionManager transactionManager(@Qualifier("activemq-db2") ActiveMQConnectionFactory activeMQConnectionFactory) {
        return new JmsTransactionManager(activeMQConnectionFactory);
    }

    // Specify Spring Transaction Management Policy
    @Bean(name = "txJmsRequired")
    public SpringTransactionPolicy propagationRequired(@Qualifier("jmsTransactionManagerMsg") PlatformTransactionManager transactionManager) {
        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
        propagationRequired.setTransactionManager(transactionManager);
        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return propagationRequired;
    }

    @Bean(name = "txCardRequired")
    public SpringTransactionPolicy propagationRequiredCard(@Qualifier("cardTransactionManager") PlatformTransactionManager transactionManager) {
        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
        propagationRequired.setTransactionManager(transactionManager);
        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return propagationRequired;
    }


    // chain transaction manager
    @Bean(name = "jtaTransactionRequired")
    public SpringTransactionPolicy jtaTransactionPolicy(final @Qualifier("chainedTransactionManager") PlatformTransactionManager chainedTransactionManager) {
        SpringTransactionPolicy springTransactionPolicy = new SpringTransactionPolicy();
        springTransactionPolicy.setTransactionManager(chainedTransactionManager);
        springTransactionPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return springTransactionPolicy;
    }

    @Bean(name = "chainedTransactionManager")
    public ChainedTransactionManager chainedTransactionManager(@Qualifier("userTransactionManager") PlatformTransactionManager userTransactionManager,
                                                               @Qualifier("cardTransactionManager") PlatformTransactionManager cardTransactionManager) {
        return new ChainedTransactionManager(userTransactionManager, cardTransactionManager);
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
