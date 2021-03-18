package com.nischit.myexp.webflux.netty.persistence;

import java.util.Optional;

import com.nischit.myexp.webflux.domain.TeamDetails;

import reactor.core.publisher.Mono;

/**
 * Persistence layer to save team information.
 */
public interface TeamPersistence {

    /**
     * Creates team.
     *
     * @param teamDetails An instance of {@link TeamDetails}.
     * @return A {@link Mono} holding {@link TeamDetails}.
     */
    Mono<TeamDetails> createTeam(TeamDetails teamDetails);

    /**
     * Returns team info.
     *
     * @param teamId Team Id
     * @return An instance of {@link Mono}.
     */
    Mono<Optional<TeamDetails>> getTeamInfo(String teamId);

    /**
     * Deletes team.
     *
     * @param teamId String
     * @return An instance of {@link Mono}.
     */
    Mono<Boolean> deleteTeam(String teamId);
}
