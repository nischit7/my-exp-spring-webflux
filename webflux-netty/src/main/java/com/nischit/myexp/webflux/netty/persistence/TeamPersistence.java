package com.nischit.myexp.webflux.netty.persistence;

import com.nischit.myexp.webflux.domain.TeamDetails;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TeamPersistence {

    Mono<TeamDetails> createTeam(final TeamDetails teamDetails);

    Mono<Optional<TeamDetails>> getTeamInfo(final String teamId);

    Mono<Boolean> deleteTeam(final String teamId);
}
