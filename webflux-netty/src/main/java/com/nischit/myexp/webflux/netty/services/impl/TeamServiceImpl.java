package com.nischit.myexp.webflux.netty.services.impl;

import com.nischit.myexp.webflux.netty.persistence.TeamPersistence;
import com.nischit.myexp.webflux.domain.TeamDetails;
import com.nischit.myexp.webflux.netty.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamPersistence teamPersistence;

    @Autowired
    public TeamServiceImpl(final TeamPersistence teamPersistence) {
        this.teamPersistence = teamPersistence;
    }

    @Override
    public Mono<TeamDetails> createTeam(TeamDetails teamDetails) {
        return this.teamPersistence.createTeam(teamDetails);
    }

    @Override
    public Mono<TeamDetails> getTeamInfo(String teamId) {
        Mono<Optional<TeamDetails>> teamDetails = teamPersistence.getTeamInfo(teamId);
        return teamDetails.map(teamDet -> teamDet.map(details -> details).orElseThrow(() -> new TeamNotFoundException()));
    }

    @Override
    public Mono<Boolean> deleteTeam(final String teamId) {
        return teamPersistence.deleteTeam(teamId);
    }
}
