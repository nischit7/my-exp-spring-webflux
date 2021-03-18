package com.nischit.myexp.webflux.netty.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nischit.myexp.webflux.domain.TeamDetails;
import com.nischit.myexp.webflux.netty.persistence.TeamPersistence;
import com.nischit.myexp.webflux.netty.services.TeamService;

import reactor.core.publisher.Mono;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamPersistence teamPersistence;

    @Autowired
    public TeamServiceImpl(final TeamPersistence teamPersistence) {
        this.teamPersistence = teamPersistence;
    }

    @Override
    public Mono<TeamDetails> createTeam(final TeamDetails teamDetails) {
        return this.teamPersistence.createTeam(teamDetails);
    }

    @Override
    public Mono<TeamDetails> getTeamInfo(final String teamId) {
        final Mono<Optional<TeamDetails>> teamDetails = teamPersistence.getTeamInfo(teamId);
        return teamDetails.map(teamDet -> teamDet.map(details -> details).orElseThrow(() -> new TeamNotFoundException()));
    }

    @Override
    public Mono<Boolean> deleteTeam(final String teamId) {
        return teamPersistence.deleteTeam(teamId);
    }
}
