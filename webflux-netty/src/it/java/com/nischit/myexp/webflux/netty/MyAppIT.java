package com.nischit.myexp.webflux.netty;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nischit.myexp.webflux.netty.app.MyApplication;
import com.nischit.myexp.webflux.support.ApiUrls;
import com.nischit.myexp.webflux.util.JsonUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;

import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = MyApplication.class,
        properties = "spring.config.location=classpath:webflux-netty-it.properties")
public class MyAppIT {

    public static final String CONTEXT_PATH = "/sample";
    public static final String MANAGE_CONTEXT = CONTEXT_PATH + "/manage";
    public static final String HEALTH_CHECK_URI = MANAGE_CONTEXT + "/health";

    public static final String BASE_SERVER_URL = "http://localhost:8080";

    @Test
    @DisplayName("When health check succeeds")
    public void healthCheckSucceeds() {

        RestAssured.given()
            .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
            .baseUri(BASE_SERVER_URL)
            .accept("application/json")
            .get(HEALTH_CHECK_URI)
            .prettyPeek()
            .then()
            .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("UP"));
    }

    @Test
    @DisplayName("When team creation succeeds")
    public void createTeamSucceeds() {
        createTeamSucceeds("teamCreateSuccess-it.json");
        deleteTeamSucceeds("myteamid");
    }

    @Test
    @DisplayName("When team creation fails as no auth header is sent")
    public void createTeamFailsForMissingAuthHeader() {
        RestAssured.given()
            .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
            .baseUri(BASE_SERVER_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(JsonUtils.convertFileToJsonString("teamCreateSuccess-it.json"))
            .log()
            .all(true)
            .post(CONTEXT_PATH + ApiUrls.TEAMS_API_URI)
            .prettyPeek()
            .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("When team creation fails as invalid auth header is sent")
    public void createTeamFailsForInvalidAuthHeader() {
        RestAssured.given()
            .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
            .baseUri(BASE_SERVER_URL)
            .header(HttpHeaders.AUTHORIZATION, "invalidAuth")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(JsonUtils.convertFileToJsonString("teamCreateSuccess-it.json"))
            .log()
            .all(true)
            .post(CONTEXT_PATH + ApiUrls.TEAMS_API_URI)
            .prettyPeek()
            .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    public void createTeamSucceeds(final String payloadFileName) {
        RestAssured.given()
            .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
            .baseUri(BASE_SERVER_URL)
            .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(JsonUtils.convertFileToJsonString(payloadFileName))
            .log()
            .all(true)
            .post(CONTEXT_PATH + ApiUrls.TEAMS_API_URI)
            .prettyPeek()
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public void deleteTeamSucceeds(final String teamId) {
        RestAssured.given()
            .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
            .baseUri(BASE_SERVER_URL)
            .header(HttpHeaders.AUTHORIZATION, buildAuthHeader())
            .log()
            .all(true)
            .delete(CONTEXT_PATH + ApiUrls.TEAMS_API_URI + "/" + teamId)
            .prettyPeek()
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private String buildAuthHeader() {
        final String jwtToken = Jwts.builder()
                .setSubject("username")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(14400000)))
                .signWith(SignatureAlgorithm.HS512, "12345678901234567890")
                .compact();

        return "Bearer " + jwtToken;
    }
}
