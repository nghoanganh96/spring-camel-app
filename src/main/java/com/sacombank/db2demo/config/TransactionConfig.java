package com.sacombank.db2demo.config;

import lombok.RequiredArgsConstructor;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class TransactionConfig {

    @Bean(name = "txAtomikosJtaRequired")
    public SpringTransactionPolicy propagationRequiredCard(@Qualifier("jtaAtomikosTransactionManager") PlatformTransactionManager jtaAtomikosTransactionManager) {
        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
        propagationRequired.setTransactionManager(jtaAtomikosTransactionManager);
        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return propagationRequired;
    }

}
