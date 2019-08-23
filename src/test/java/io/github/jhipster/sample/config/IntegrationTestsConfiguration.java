package io.github.jhipster.sample.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;


import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@Configuration
@Profile("testcontainers")
public class IntegrationTestsConfiguration {

    private Logger logger = LoggerFactory.getLogger(IntegrationTestsConfiguration.class);

    private JdbcDatabaseContainer dbContainer;

    @Bean
    public DataSource dataSource() {
        dbContainer = new PostgreSQLContainer("postgis:9.4")
            .withUsername("jhipstersampleapplication")
            .withPassword("");
        String jdbcUrlSuffix = "";

        dbContainer.start();

        String jdbcUrl = dbContainer.getJdbcUrl() + jdbcUrlSuffix;
        logger.info("Database started, creating datasource for url: '{}'", jdbcUrl);


        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(dbContainer.getUsername());
        dataSource.setPassword(dbContainer.getPassword());
        dataSource.setDriverClassName(dbContainer.getDriverClassName());
        dataSource.setAutoCommit(false);

        return dataSource;

    }

    @PreDestroy
    public void preDestroy(){
        if(this.dbContainer != null){
            this.dbContainer.stop();
        }
    }

}
