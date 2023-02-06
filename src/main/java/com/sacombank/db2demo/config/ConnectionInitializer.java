package com.sacombank.db2demo.config;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConnectionInitializer {
    @Value("${activemq-jds.name}")
    private String activeMQName;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    @Qualifier("activemqComponent")
    private org.apache.camel.Component activemqComponent;

    @Bean
    public void loadCamelComponent() {
        // activeMQName using in Camel Routes
        camelContext.addComponent(activeMQName, activemqComponent);
        // camelContext.getComponent("sql", SqlComponent.class).setDataSource(dataSource);
    }
}
