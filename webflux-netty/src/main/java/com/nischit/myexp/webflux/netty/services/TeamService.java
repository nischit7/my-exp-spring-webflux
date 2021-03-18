package com.nischit.myexp.webflux.netty.services;

import com.nischit.myexp.webflux.domain.TeamDetails;

import reactor.core.publisher.Mono;

/**
 * A service class that manages team information.
 */
public interface TeamService {

    /**
     * Creates a team.
     *
     * @param teamDetails An instance of {@link TeamDetails}.
     * @return An instance of {@link Mono}
     */
    Mono<TeamDetails> createTeam(TeamDetails teamDetails);

    /**
     * Returns the team info as {@link TeamDetails}.
     *
     * @param teamId Team id
     * @return An instance of {@link Mono}
     */
    Mono<TeamDetails> getTeamInfo(String teamId);

    /**
     * Deletes the team.
     *
     * @param teamId Team id
     * @return An instance of {@link Mono}
     */
    Mono<Boolean> deleteTeam(String teamId);
}
