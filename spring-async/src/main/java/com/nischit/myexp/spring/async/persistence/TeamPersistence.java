package com.nischit.myexp.spring.async.persistence;

import com.nischit.myexp.webflux.domain.TeamDetails;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TeamPersistence {

    TeamDetails createTeam(final TeamDetails teamDetails);

    Optional<TeamDetails> getTeamInfo(final String teamId);

    boolean deleteTeam(final String teamId);
}
