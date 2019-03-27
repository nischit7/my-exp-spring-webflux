package com.nischit.myexp.spring.async.persistence.sql.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.nischit.myexp.spring.async.persistence.sql")
public class SqlDBConfig {
}
