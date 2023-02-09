package com.sacombank.db2demo.config;

import com.atomikos.jms.AtomikosConnectionFactoryBean;
import org.apache.activemq.ActiveMQXAConnectionFactory;
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
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${activemq-jds.host}")
    private String host;

    @Value("${activemq-jds.session-cache-size}")
    private int sessionCacheSize;

    @Value("${activemq-jds.name}")
    private String name;

    @Bean(name = "activemq-xa")
    public AtomikosConnectionFactoryBean activeMQXAConnectionFactory() {
        ActiveMQXAConnectionFactory connectionFactory = new ActiveMQXAConnectionFactory();
        connectionFactory.setBrokerURL(host);
        connectionFactory.setUseAsyncSend(true);
        connectionFactory.setTrustAllPackages(true);
        connectionFactory.setDispatchAsync(false);
        connectionFactory.setOptimizeAcknowledge(true);
        connectionFactory.setAlwaysSessionAsync(true);

        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(0);
        redeliveryPolicy.setRedeliveryDelay(2000);

        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);

        AtomikosConnectionFactoryBean atomikosConnectionFactoryBean = new AtomikosConnectionFactoryBean();
        atomikosConnectionFactoryBean.setUniqueResourceName(name);
        atomikosConnectionFactoryBean.setLocalTransactionMode(false);
        atomikosConnectionFactoryBean.setXaConnectionFactory(connectionFactory);

        return atomikosConnectionFactoryBean;
    }

    @Bean(name = "cacheActiveMQConnectionFactory")
    public ConnectionFactory connectionFactory(@Qualifier("activemq-xa") AtomikosConnectionFactoryBean activeMQXAConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(activeMQXAConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        cachingConnectionFactory.setReconnectOnException(true);

        return cachingConnectionFactory;
    }

    @Bean(name = "activemqComponent")
    public Component activeMQComponent(@Qualifier("cacheActiveMQConnectionFactory") ConnectionFactory connectionFactory
    , @Qualifier("jtaAtomikosTransactionManager") PlatformTransactionManager jtaAtomikosTransactionManager) {
        var jmsComponent = JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
        jmsComponent.setTransactionManager(jtaAtomikosTransactionManager);
        jmsComponent.setTransacted(true);
        return jmsComponent;
    }

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier("cacheActiveMQConnectionFactory") ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDeliveryPersistent(false);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(@Qualifier("cacheActiveMQConnectionFactory") ConnectionFactory connectionFactory
            , @Qualifier("jtaAtomikosTransactionManager") PlatformTransactionManager jtaAtomikosTransactionManager) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setTransactionManager(jtaAtomikosTransactionManager);
        factory.setSessionTransacted(true);
        // true: using jms topic, false: using jms queue
        // factory.setPubSubDomain(true);
        return factory;
    }
}
