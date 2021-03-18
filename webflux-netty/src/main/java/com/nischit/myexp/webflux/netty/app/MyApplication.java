package com.nischit.myexp.webflux.netty.app;

import org.example.webflux.localization.config.EnableLocalization;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Starter application.
 */
//CHECKSTYLE:OFF
@SpringBootApplication
@EnableLocalization
@ComponentScan(basePackages = {
    "com.nischit.myexp.webflux.netty.api",
    "com.nischit.myexp.webflux.netty.persistence",
    "com.nischit.myexp.webflux.netty.security",
    "com.nischit.myexp.webflux.netty.services"})
public class MyApplication {

    /**
     * The started method.
     *
     * @param args An array of {@link String}.
     */
    public static void main(final String[] args) {
        final SpringApplication application = new SpringApplication(MyApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
//CHECKSTYLE:ON
