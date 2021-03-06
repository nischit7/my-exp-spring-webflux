package com.nischit.myexp.webflux.netty.api;

import com.nischit.myexp.webflux.domain.TeamDetails;
import com.nischit.myexp.webflux.netty.services.TeamService;
import com.nischit.myexp.webflux.support.ApiUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApiUrls.TEAMS_API_URI)
public class TeamController {

    public static final String TEAM_REQUEST_API_VERSION = "application/vnd.nischit.myservice.teams-add-v1+json";
    public static final String TEAM_RESPONSE_API_VERSION = "application/vnd.nischit.myservice.teams-response-v1+json";

    private static final String TEAM_ID = "teamId";
    private static final String TEAM_ID_URI = "/{" + TEAM_ID + "}";

    private final TeamService teamService;

    @Autowired
    public TeamController(final TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping(consumes = TEAM_REQUEST_API_VERSION)
    public Mono<ResponseEntity<Void>> createTeam(@RequestBody final TeamDetails teamDetails) {
        System.out.println(String.format("Thread executing this task %s", Thread.currentThread()));
        return teamService.createTeam(teamDetails)
                .flatMap(details -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    @GetMapping(value = TEAM_ID_URI, produces = TEAM_RESPONSE_API_VERSION)
    public Mono<ResponseEntity<TeamDetails>> getTeamInfo(@PathVariable(TEAM_ID) final String teamId) {
        final Mono<TeamDetails> teamDetails = teamService.getTeamInfo(teamId);
        return teamDetails.map(details -> new ResponseEntity<>(details, HttpStatus.OK));
    }

    @DeleteMapping(value = TEAM_ID_URI, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseEntity<Void>> deleteTeam(@PathVariable(TEAM_ID) final String teamId) {
        return teamService.deleteTeam(teamId)
                .flatMap(status -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }
}
