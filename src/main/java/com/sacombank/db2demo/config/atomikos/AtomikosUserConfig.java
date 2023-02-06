package com.sacombank.db2demo.config.atomikos;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.sacombank.db2demo.service.EncryptService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.sacombank.db2demo.repository.user"},
        entityManagerFactoryRef = "atomikosUserEntityManagerFactory",
        transactionManagerRef = "jtaAtomikosTransactionManager"
)
@RequiredArgsConstructor
@Slf4j
public class AtomikosUserConfig {

    private final EncryptService encryptService;

    @Value("${spring.user-datasource.hbm2ddl-auto: }")
    String hbm2ddlAuto;

    @Bean(name = "atomikosMysqlUser")
    @ConfigurationProperties(prefix = "spring.jta.atomikos.datasource.mysql-user")
    public DataSource atomikosMysqlUser() {
        return new AtomikosDataSourceBean();
    }


    @Bean(name = "atomikosUserEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean atomikosUserEntityManagerFactory(@Qualifier("atomikosMysqlUser") DataSource atomikosMysqlUser) {

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        if(Strings.isNotBlank(hbm2ddlAuto)) {
            jpaProperties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        }

        entityManager.setDataSource(atomikosMysqlUser);
        entityManager.setJpaProperties(jpaProperties);
        entityManager.setPackagesToScan("com.sacombank.db2demo.entity.user");
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return entityManager;
    }
}
