package com.nischit.myexp.webflux.netty.services;

import com.nischit.myexp.webflux.domain.TeamDetails;
import reactor.core.publisher.Mono;

public interface TeamService {

    Mono<TeamDetails> createTeam(final TeamDetails teamDetails);

    Mono<TeamDetails> getTeamInfo(final String teamId);

    Mono<Boolean> deleteTeam(final String teamId);
}
