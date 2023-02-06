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
    /*
        // Create Transaction Manager
        @Bean("jmsTransactionManagerMsg")
        public PlatformTransactionManager transactionManager(@Qualifier("activemq-db2") ActiveMQConnectionFactory activeMQConnectionFactory) {
            return new JmsTransactionManager(activeMQConnectionFactory);
        }

        // Specify Spring Transaction Management Policy
        @Bean(name = "txJmsRequired")
        public SpringTransactionPolicy propagationRequired(@Qualifier("jmsTransactionManagerMsg") PlatformTransactionManager jmsTransactionManagerMsg) {
            SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
            propagationRequired.setTransactionManager(jmsTransactionManagerMsg);
            propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
            return propagationRequired;
        }
    */


    @Bean(name = "txAtomikosJtaRequired")
    public SpringTransactionPolicy propagationRequiredCard(@Qualifier("jtaAtomikosTransactionManager") PlatformTransactionManager jtaAtomikosTransactionManager) {
        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
        propagationRequired.setTransactionManager(jtaAtomikosTransactionManager);
        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return propagationRequired;
    }

//
//    @Bean(name = "txCardRequired")
//    public SpringTransactionPolicy propagationRequiredCard(@Qualifier("cardTransactionManager") PlatformTransactionManager transactionManager) {
//        SpringTransactionPolicy propagationRequired = new SpringTransactionPolicy();
//        propagationRequired.setTransactionManager(transactionManager);
//        propagationRequired.setPropagationBehaviorName("PROPAGATION_REQUIRED");
//        return propagationRequired;
//    }
//
//    // chain transaction manager
//    @Bean(name = "jtaTransactionRequired")
//    public SpringTransactionPolicy jtaTransactionPolicy(final @Qualifier("chainedTransactionManager") PlatformTransactionManager chainedTransactionManager) {
//        SpringTransactionPolicy springTransactionPolicy = new SpringTransactionPolicy();
//        springTransactionPolicy.setTransactionManager(chainedTransactionManager);
//        springTransactionPolicy.setPropagationBehaviorName("PROPAGATION_REQUIRED");
//        return springTransactionPolicy;
//    }
//
//    @Bean(name = "chainedTransactionManager")
//    public ChainedTransactionManager chainedTransactionManager(@Qualifier("userTransactionManager") PlatformTransactionManager userTransactionManager,
//                                                               @Qualifier("cardTransactionManager") PlatformTransactionManager cardTransactionManager) {
//        return new ChainedTransactionManager(userTransactionManager, cardTransactionManager);
//    }

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
