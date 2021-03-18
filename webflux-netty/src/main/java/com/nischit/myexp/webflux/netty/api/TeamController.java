package com.nischit.myexp.webflux.netty.api;

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

import com.nischit.myexp.webflux.domain.TeamDetails;
import com.nischit.myexp.webflux.netty.services.TeamService;
import com.nischit.myexp.webflux.support.ApiUrls;

import reactor.core.publisher.Mono;

/**
 * Team controller REST API.
 */
@RestController
@RequestMapping(ApiUrls.TEAMS_API_URI)
public class TeamController {

    private static final String TEAM_ID = "teamId";
    private static final String TEAM_ID_URI = "/{" + TEAM_ID + "}";

    private final TeamService teamService;

    @Autowired
    public TeamController(final TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Creates the team using details provided by {@link TeamDetails}.
     *
     * @param teamDetails An instance of {@link TeamDetails}.
     * @return An instance of {@link ResponseEntity}.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> createTeam(@RequestBody final TeamDetails teamDetails) {
        System.out.println(String.format("Thread executing this task %s", Thread.currentThread()));
        return teamService.createTeam(teamDetails)
                .flatMap(details -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    /**
     * Returns team information.
     *
     * @param teamId Represents the team id.
     * @return An instance of {@link ResponseEntity} holding {@link TeamDetails}.
     */
    @GetMapping(value = TEAM_ID_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TeamDetails>> getTeamInfo(@PathVariable(TEAM_ID) final String teamId) {
        final Mono<TeamDetails> teamDetails = teamService.getTeamInfo(teamId);
        return teamDetails.map(details -> new ResponseEntity<>(details, HttpStatus.OK));
    }

    /**
     * Deletes the team information.
     *
     * @param teamId Represents the team id.
     * @return An instance of {@link ResponseEntity}.
     */
    @DeleteMapping(value = TEAM_ID_URI, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> deleteTeam(@PathVariable(TEAM_ID) final String teamId) {
        return teamService.deleteTeam(teamId)
                .flatMap(status -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }
}
