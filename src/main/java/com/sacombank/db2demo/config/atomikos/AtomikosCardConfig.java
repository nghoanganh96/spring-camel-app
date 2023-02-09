package com.sacombank.db2demo.config.atomikos;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.sacombank.db2demo.service.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.sacombank.db2demo.repository.card"},
        entityManagerFactoryRef = "atomikosCardEntityManagerFactory",
        transactionManagerRef = "jtaAtomikosTransactionManager" // point to bean at AtomikosTransactionConfig
)
@RequiredArgsConstructor
@Slf4j
public class AtomikosCardConfig {

    private final EncryptService encryptService;

    @Value("${spring.jta.atomikos.datasource.db2-card.hbm2ddl-auto: }")
    String hbm2ddlAuto;

    @Primary
    @Bean(name = "atomikosDb2CardTemp")
    @ConfigurationProperties(prefix = "spring.jta.atomikos.datasource.db2-card")
    public AtomikosDataSourceBean atomikosDb2CardTemp() {
        // automatically inject properties from application.yml
        return new AtomikosDataSourceBean();
    }

    @Primary
    @Bean(name = "atomikosDb2Card")
    public DataSource atomikosDb2Card() {
        var dataSourceBean = atomikosDb2CardTemp();
        Properties properties = dataSourceBean.getXaProperties();
        // Decode password
        properties.setProperty("password", encryptService.decode(properties.getProperty("password")));
        return dataSourceBean;
    }

    @Primary
    @Bean(name = "atomikosCardEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean atomikosCardEntityManagerFactory(
            @Qualifier("atomikosDb2Card") DataSource atomikosDb2Card) {

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.DB2Dialect");
        if (Strings.isNotBlank(hbm2ddlAuto)) {
            jpaProperties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        }

        entityManager.setDataSource(atomikosDb2Card);
        entityManager.setJpaProperties(jpaProperties);
        entityManager.setPackagesToScan("com.sacombank.db2demo.entity.card", "com.sacombank.db2demo.entity.base"); // point to DB2 Entities
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return entityManager;
    }
}
