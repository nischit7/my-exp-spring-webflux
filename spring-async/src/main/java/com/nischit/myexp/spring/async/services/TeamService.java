package com.nischit.myexp.spring.async.services;

import com.nischit.myexp.webflux.domain.TeamDetails;
import reactor.core.publisher.Mono;

public interface TeamService {

    TeamDetails createTeam(final TeamDetails teamDetails);

    TeamDetails getTeamInfo(final String teamId);

    boolean deleteTeam(final String teamId);
}
