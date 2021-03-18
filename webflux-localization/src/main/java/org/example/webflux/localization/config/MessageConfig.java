package org.example.webflux.localization.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Config for localization.
 */
@Configuration
@ComponentScan(basePackages = "org.example.localization")
public class MessageConfig {

    /**
     * Bean for Non Error Messages.
     * Looks for Files Messages_{Locale} in the classpath.
     * Example:Messages_en_US.properties.
     */
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:Messages");
        return messageSource;
    }

    /**
     * Bean for  Error Messages.
     * Looks for Files ErrorMessages_{Locale} in the classpath.
     * Example:ErrorMessages_en_US.properties.
     */
    @Bean(name = "errorMessageSource")
    public MessageSource errorMessageSource() {
        final ReloadableResourceBundleMessageSource errorMessageSource = new ReloadableResourceBundleMessageSource();
        errorMessageSource.setBasename("classpath:ErrorMessages");
        return errorMessageSource;
    }
}
