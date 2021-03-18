package com.nischit.myexp.spring.async.persistence.sql.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SQL configuration.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.nischit.myexp.spring.async.persistence.sql")
public class SqlDBConfig {

    private final String hibernateDdl;

    private final String dialect;

    private final Boolean showSql;

    private final String defaultSchema;

    private final String batchSize;

    private final Boolean orderInserts;

    private final Boolean orderUpdates;

    private final Boolean batchVersionedData;

    @Autowired
    public SqlDBConfig(
            @Value("${datastore.jdbc.hibernate.hbm2ddl.auto:create}") final String hibernateDdl,
            @Value("${datastore.jdbc.hibernate.dialect:org.hibernate.dialect.H2Dialect}") final String dialect,
            @Value("${hibernate.show-sql:false}") final Boolean showSql,
            @Value("${datastore.jdbc.hibernate.default-schema:public}") final String defaultSchema,
            @Value("${hibernate.jdbc.batch_size:5}") final String batchSize,
            @Value("${hibernate.order_inserts:true}") final Boolean orderInserts,
            @Value("${hibernate.order_updates:true}") final Boolean orderUpdates,
            @Value("${hibernate.batch_versioned_data:true}") final Boolean batchVersionedData) {

        this.hibernateDdl = hibernateDdl;
        this.dialect = dialect;
        this.showSql = showSql;
        this.defaultSchema = defaultSchema;
        this.batchSize = batchSize;
        this.orderInserts = orderInserts;
        this.orderUpdates = orderUpdates;
        this.batchVersionedData = batchVersionedData;
    }

    /**
     * Declare the JPA entity manager factory.
     *
     * @param dataSource {@link DataSource}.
     * @return {@link LocalContainerEntityManagerFactoryBean}.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);

        // Classpath scanning of @Component, @Service, etc annotated class
        entityManagerFactory.setPackagesToScan(
                "com.nischit.myexp.spring.async.persistence.sql");

        // Vendor adapter
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        // Hibernate properties
        final Properties additionalProperties = additionalProperties();
        entityManagerFactory.setJpaProperties(additionalProperties);

        return entityManagerFactory;
    }

    private Properties additionalProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", this.hibernateDdl);
        properties.setProperty("hibernate.show_sql", this.showSql.toString());
        properties.setProperty("hibernate.dialect", this.dialect);
        properties.setProperty("hibernate.default_schema", this.defaultSchema);
        properties.setProperty("hibernate.jdbc.batch_size", this.batchSize);
        properties.setProperty("hibernate.order_inserts", this.orderInserts.toString());
        properties.setProperty("hibernate.order_updates", this.orderUpdates.toString());
        properties.setProperty("hibernate.batch_versioned_data", this.batchVersionedData.toString());
        return properties;
    }
}
