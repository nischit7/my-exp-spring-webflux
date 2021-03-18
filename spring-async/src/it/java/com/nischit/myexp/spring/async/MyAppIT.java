package com.nischit.myexp.spring.async;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nischit.myexp.spring.async.app.MyApplication;
import com.nischit.myexp.webflux.support.ApiUrls;
import com.nischit.myexp.webflux.util.JsonUtils;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;

/**
 * Integration tests.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = MyApplication.class,
        properties = "spring.config.location=classpath:spring-async-it.properties")
public class MyAppIT {

    public static final String CONTEXT_PATH = "/sample";
    public static final String MANAGE_CONTEXT = CONTEXT_PATH + "/manage";

    public static final String HEALTH_CHECK_URL = MANAGE_CONTEXT + "/health";

    public static final String BASE_HEALTH_SERVER_URL = "http://localhost:8080";
    public static final String BASE_SERVER_URL = "http://localhost:8080";

    @Test
    @DisplayName("When team creation succeeds")
    public void createTeamSucceeds() {
        createTeamSucceeds("teamCreateSuccess-it.json");
        deleteTeamSucceeds("myteamid");
    }

    public void createTeamSucceeds(final String payloadFileName) {
        RestAssured.given()
            .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
            .baseUri(BASE_SERVER_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(JsonUtils.convertFileToJsonString(payloadFileName))
            .log()
            .all(true)
            .post(ApiUrls.TEAMS_API_URI)
            .prettyPeek()
            .then()
                .statusCode(HttpStatus.OK.value());
    }

    public void deleteTeamSucceeds(final String teamId) {
        RestAssured.given()
            .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
            .baseUri(BASE_SERVER_URL)
            .log()
            .all(true)
            .delete(ApiUrls.TEAMS_API_URI + "/" + teamId)
            .prettyPeek()
            .then()
                .statusCode(HttpStatus.OK.value());
    }
}
