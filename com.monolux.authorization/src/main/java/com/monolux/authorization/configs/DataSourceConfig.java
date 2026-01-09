package com.monolux.authorization.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public final static String BEAN_NAME_HIKARI_CONFIG_AUTHORIZATION = "spring.datasource.hikari.authorization";

    public final static String BEAN_NAME_DATASOURCE_AUTHORIZATION = "authorizationDataSource";

    public final static String BEAN_NAME_JDBC_TEMPLATE_AUTHORIZATION = "authorizationJdbcTemplate";

    // endregion

    // region ▒▒▒▒▒ Beans ▒▒▒▒▒

    @ConfigurationProperties(prefix = DataSourceConfig.BEAN_NAME_HIKARI_CONFIG_AUTHORIZATION)
    @Qualifier(DataSourceConfig.BEAN_NAME_HIKARI_CONFIG_AUTHORIZATION)
    @Bean(name = DataSourceConfig.BEAN_NAME_HIKARI_CONFIG_AUTHORIZATION)
    public HikariConfig hikariConfigAuthorization() {
        return new HikariConfig();
    }

    @Qualifier(DataSourceConfig.BEAN_NAME_DATASOURCE_AUTHORIZATION)
    @Bean(name = DataSourceConfig.BEAN_NAME_DATASOURCE_AUTHORIZATION)
    public DataSource dataSourceAuthorization(@Qualifier(DataSourceConfig.BEAN_NAME_HIKARI_CONFIG_AUTHORIZATION) final HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Qualifier(DataSourceConfig.BEAN_NAME_JDBC_TEMPLATE_AUTHORIZATION)
    @Bean(name = DataSourceConfig.BEAN_NAME_JDBC_TEMPLATE_AUTHORIZATION)
    public JdbcTemplate jdbcTemplateAuthorization(@Qualifier(DataSourceConfig.BEAN_NAME_DATASOURCE_AUTHORIZATION) final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // endregion
}