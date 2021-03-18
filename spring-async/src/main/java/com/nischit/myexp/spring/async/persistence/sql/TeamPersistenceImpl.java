package com.nischit.myexp.spring.async.persistence.sql;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nischit.myexp.spring.async.persistence.TeamPersistence;
import com.nischit.myexp.spring.async.persistence.sql.entity.TeamDetailsEntity;
import com.nischit.myexp.spring.async.persistence.sql.repository.TeamDetailsRepository;
import com.nischit.myexp.webflux.domain.TeamDetails;

/**
 * An implementation of {@link TeamPersistence}.
 */
@Repository
public class TeamPersistenceImpl implements TeamPersistence {

    private final TeamDetailsRepository teamDetailsRepository;

    @Autowired
    public TeamPersistenceImpl(final TeamDetailsRepository teamDetailsRepository) {
        this.teamDetailsRepository = teamDetailsRepository;
    }

    @Override
    public TeamDetails createTeam(final TeamDetails teamDetails) {
        final TeamDetailsEntity teamDetailsEntity = TeamDetailsEntity.builder()
                .teamId(teamDetails.getTeamId())
                .teamName(teamDetails.getTeamName())
                .teamDesc(teamDetails.getTeamDesc())
                .build();
        // Dont make blocking call as below. This is just a demo
        this.teamDetailsRepository.save(teamDetailsEntity);
        return teamDetails;
    }

    @Override
    public Optional<TeamDetails> getTeamInfo(final String teamId) {
        final Optional<TeamDetailsEntity> teamEntity = this.teamDetailsRepository.findById(teamId);
        return teamEntity.map(entity -> Optional.of(TeamDetails.builder()
                .teamId(entity.getTeamId())
                .teamName(entity.getTeamName())
                .teamDesc(entity.getTeamDesc())
                .build()))
                .orElse(Optional.empty());
    }

    @Override
    public boolean deleteTeam(final String teamId) {
        // Dont make blocking call as below. This is just a demo
        this.teamDetailsRepository.deleteById(teamId);
        return Boolean.TRUE;
    }
}
