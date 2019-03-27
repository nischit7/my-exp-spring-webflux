package com.nischit.myexp.spring.async.persistence.sql;

import com.nischit.myexp.spring.async.persistence.TeamPersistence;
import com.nischit.myexp.spring.async.persistence.sql.entity.TeamDetailsEntity;
import com.nischit.myexp.spring.async.persistence.sql.repository.TeamDetailsRepository;
import com.nischit.myexp.webflux.domain.TeamDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public class TeamPersistenceImpl implements TeamPersistence {

    private final TeamDetailsRepository teamDetailsRepository;

    @Autowired
    public TeamPersistenceImpl(final TeamDetailsRepository teamDetailsRepository) {
        this.teamDetailsRepository = teamDetailsRepository;
    }

    @Override
    public TeamDetails createTeam(TeamDetails teamDetails) {
        final TeamDetailsEntity teamDetailsEntity = new TeamDetailsEntity.Builder()
                .teamId(teamDetails.getTeamId())
                .teamName(teamDetails.getTeamName())
                .teamDesc(teamDetails.getTeamDesc())
                .build();
        // Dont make blocking call as below. This is just a demo
        teamDetailsRepository.save(teamDetailsEntity);
        return teamDetails;
    }

    @Override
    public Optional<TeamDetails> getTeamInfo(final String teamId) {
        final Optional<TeamDetailsEntity> teamEntity = teamDetailsRepository.findById(teamId);
        return teamEntity.map(entity -> Optional.of(new TeamDetails.Builder()
                .teamId(entity.getTeamId())
                .teamName(entity.getTeamName())
                .teamDesc(entity.getTeamDesc())
                .build()))
                .orElse(Optional.empty());
    }

    @Override
    public boolean deleteTeam(final String teamId) {
        // Dont make blocking call as below. This is just a demo
        teamDetailsRepository.deleteById(teamId);
        return Boolean.TRUE;
    }
}
