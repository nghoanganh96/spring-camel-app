package com.sacombank.db2demo.config.dbconfig;

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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.sacombank.db2demo.repository.user"},
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager"
)
@RequiredArgsConstructor
@Slf4j
public class UserConfig {

    private final EncryptService encryptService;

    @Value("${spring.user-datasource.hbm2ddl-auto: }")
    String hbm2ddlAuto;

    @Bean(name = "userDataSourceProperties")
    @ConfigurationProperties("spring.user-datasource")
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "userDataSource")
    @ConfigurationProperties("spring.user-datasource.configuration")
    public DataSource cardDataSource(@Qualifier("userDataSourceProperties") DataSourceProperties userDataSourceProperties) {
        var dataSource = userDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();

        dataSource.setPassword(encryptService.decode(dataSource.getPassword()));

        return dataSource;
    }

    @Bean(name = "userEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
            EntityManagerFactoryBuilder builder
            , @Qualifier("userDataSource") DataSource userDataSource) {

        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        if(Strings.isNotBlank(hbm2ddlAuto)) {
            properties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        }

        return builder
                .dataSource(userDataSource)
                .packages("com.sacombank.db2demo.entity.user")
                .properties(properties)
                .build();
    }

    @Bean(name = "userTransactionManager")
    public PlatformTransactionManager userTransactionManager(
            @Qualifier("userEntityManagerFactory") EntityManagerFactory userEntityManagerFactory) {

        return new JpaTransactionManager(userEntityManagerFactory);
    }
}
