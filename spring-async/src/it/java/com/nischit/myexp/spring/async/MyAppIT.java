package com.nischit.myexp.spring.async;

import com.nischit.myexp.spring.async.api.TeamController;
import com.nischit.myexp.webflux.util.JsonUtils;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.nischit.myexp.webflux.support.ApiUrls.TEAMS_API_URI;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {MyApplication.class},
        properties = {
                "spring.config.location=classpath:spring-async-it.properties"
        })
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
                given()
                        .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().sslConfig().relaxedHTTPSValidation()))
                        .baseUri(BASE_SERVER_URL)
                        .contentType(TeamController.TEAM_REQUEST_API_VERSION)
                        .body(JsonUtils.convertFileToJsonString(payloadFileName))
                        .log()
                        .all(true)
                        .post(TEAMS_API_URI)
                        .prettyPeek()
                        .then()
                        .statusCode(NO_CONTENT.value());
        }

        public void deleteTeamSucceeds(final String teamId) {
                given()
                        .config(RestAssured.config().sslConfig(SSLConfig.sslConfig().sslConfig().relaxedHTTPSValidation()))
                        .baseUri(BASE_SERVER_URL)
                        .log()
                        .all(true)
                        .delete(TEAMS_API_URI + "/" + teamId)
                        .prettyPeek()
                        .then()
                        .statusCode(NO_CONTENT.value());
        }
}
