package com.sacombank.db2demo.config.atomikos;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class AtomikosTransactionConfig {


    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    public UserTransactionManager atomikosTransactionManager() throws SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        userTransactionManager.setTransactionTimeout(3000);
        return userTransactionManager;
    }

    @Bean(name = "jtaAtomikosTransactionManager")
    public PlatformTransactionManager jtaAtomikosTransactionManager()  throws Throwable {
        return new JtaTransactionManager(atomikosTransactionManager(), atomikosTransactionManager());
    }
}
