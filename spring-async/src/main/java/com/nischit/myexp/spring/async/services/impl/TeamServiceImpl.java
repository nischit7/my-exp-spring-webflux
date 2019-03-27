package com.nischit.myexp.spring.async.services.impl;

import com.nischit.myexp.spring.async.persistence.TeamPersistence;
import com.nischit.myexp.spring.async.services.TeamService;
import com.nischit.myexp.webflux.domain.TeamDetails;
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
    public TeamDetails createTeam(TeamDetails teamDetails) {
        return this.teamPersistence.createTeam(teamDetails);
    }

    @Override
    public TeamDetails getTeamInfo(String teamId) {
        Optional<TeamDetails> teamDetails = teamPersistence.getTeamInfo(teamId);
        return teamDetails.map(details -> details).orElseThrow(() -> new TeamNotFoundException());
    }

    @Override
    public boolean deleteTeam(final String teamId) {
        return teamPersistence.deleteTeam(teamId);
    }
}
