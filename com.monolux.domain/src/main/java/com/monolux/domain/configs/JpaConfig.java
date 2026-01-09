package com.monolux.domain.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = JpaConfig.BASE_PACKAGE,
        entityManagerFactoryRef = JpaConfig.BEAN_NAME_ENTITY_MANAGER_FACTORY,
        transactionManagerRef = JpaConfig.BEAN_NAME_TRANSACTION_MANAGER)
public class JpaConfig {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    // region ▶ Package Names ◀

    protected final static String BASE_PACKAGE = "com.monolux.domain";

    // endregion

    // region ▶ Config Property Paths ◀

    private final static String PREFIX_DATASOURCE_HIKARI_DOMAIN = "spring.datasource.hikari.domain";

    private final static String PREFIX_SPRING_JPA = "spring.jpa";

    private final static String PREFIX_SPRING_JPA_HIBERNATE = "spring.jpa.hibernate";

    // endregion

    // region ▶ Bean Names ◀

    private final static String BEAN_NAME_DATASOURCE_HIKARI_DOMAIN = "datasourceHikariDomain";

    private final static String BEAN_NAME_DATA_SOURCE_DOMAIN = "dataSourceJpa";

    private final static String BEAN_NAME_JPA_PROPERTIES = "jpaPropertiesMaster";

    private final static String BEAN_NAME_HIBERNATE_PROPERTIES = "hibernatePropertiesMaster";

    private final static String BEAN_NAME_JPA_VENDOR_ADAPTER = "jpaVendorAdapterMaster";

    private final static String BEAN_NAME_ENTITY_MANAGER_FACTORY_BUILDER = "entityManagerFactoryBuilderMaster";

    protected final static String BEAN_NAME_ENTITY_MANAGER_FACTORY = "entityManagerFactoryMaster";

    protected final static String BEAN_NAME_TRANSACTION_MANAGER = "transactionManagerMaster";

    // endregion

    // endregion

    // region ▒▒▒▒▒ Beans ▒▒▒▒▒

    @ConfigurationProperties(prefix = JpaConfig.PREFIX_DATASOURCE_HIKARI_DOMAIN)
    @Bean(name = JpaConfig.BEAN_NAME_DATASOURCE_HIKARI_DOMAIN)
    @Qualifier(JpaConfig.BEAN_NAME_DATASOURCE_HIKARI_DOMAIN)
    public HikariConfig hikariConfigDomain() {
        return new HikariConfig();
    }

    @Bean(name = JpaConfig.BEAN_NAME_DATA_SOURCE_DOMAIN)
    @Qualifier(JpaConfig.BEAN_NAME_DATA_SOURCE_DOMAIN)
    public DataSource dataSourceDomain(@Qualifier(JpaConfig.BEAN_NAME_DATASOURCE_HIKARI_DOMAIN) final HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Primary
    @ConfigurationProperties(prefix = JpaConfig.PREFIX_SPRING_JPA)
    @Bean(name = JpaConfig.BEAN_NAME_JPA_PROPERTIES)
    @Qualifier(JpaConfig.BEAN_NAME_JPA_PROPERTIES)
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    @Primary
    @ConfigurationProperties(prefix = JpaConfig.PREFIX_SPRING_JPA_HIBERNATE)
    @Bean(name = JpaConfig.BEAN_NAME_HIBERNATE_PROPERTIES)
    @Qualifier(JpaConfig.BEAN_NAME_HIBERNATE_PROPERTIES)
    public HibernateProperties hibernateProperties() {
        return new HibernateProperties();
    }

    @Primary
    @Bean(name = JpaConfig.BEAN_NAME_JPA_VENDOR_ADAPTER)
    @Qualifier(JpaConfig.BEAN_NAME_JPA_VENDOR_ADAPTER)
    public JpaVendorAdapter jpaVendorAdapter(@Qualifier(JpaConfig.BEAN_NAME_JPA_PROPERTIES) final JpaProperties jpaProperties) {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(jpaProperties.getDatabase());
        adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        adapter.setShowSql(jpaProperties.isShowSql());
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return adapter;
    }

    @Primary
    @Bean(name = JpaConfig.BEAN_NAME_ENTITY_MANAGER_FACTORY_BUILDER)
    @Qualifier(JpaConfig.BEAN_NAME_ENTITY_MANAGER_FACTORY_BUILDER)
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
            @Qualifier(JpaConfig.BEAN_NAME_JPA_VENDOR_ADAPTER) final JpaVendorAdapter jpaVendorAdapter,
            @Qualifier(JpaConfig.BEAN_NAME_JPA_PROPERTIES) final JpaProperties jpaProperties
    ) {
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(),null);
    }

    @Primary
    @Bean(name = JpaConfig.BEAN_NAME_ENTITY_MANAGER_FACTORY)
    @Qualifier(JpaConfig.BEAN_NAME_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(JpaConfig.BEAN_NAME_DATA_SOURCE_DOMAIN) final DataSource dataSource,
            @Qualifier(JpaConfig.BEAN_NAME_JPA_PROPERTIES) final JpaProperties jpaProperties,
            @Qualifier(JpaConfig.BEAN_NAME_HIBERNATE_PROPERTIES) final HibernateProperties hibernateProperties,
            @Qualifier(JpaConfig.BEAN_NAME_ENTITY_MANAGER_FACTORY_BUILDER) final EntityManagerFactoryBuilder entityManagerFactoryBuilder
    ) {
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());

        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .properties(properties)
                .packages(JpaConfig.BASE_PACKAGE)
                .persistenceUnit(JpaConfig.BEAN_NAME_DATA_SOURCE_DOMAIN)
                .build();
    }

    @Primary
    @Bean(name = JpaConfig.BEAN_NAME_TRANSACTION_MANAGER)
    @Qualifier(JpaConfig.BEAN_NAME_TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(
            @Qualifier(JpaConfig.BEAN_NAME_ENTITY_MANAGER_FACTORY) final EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new com.monolux.domain.auditors.AuditorAware();
    }

    // endregion
}