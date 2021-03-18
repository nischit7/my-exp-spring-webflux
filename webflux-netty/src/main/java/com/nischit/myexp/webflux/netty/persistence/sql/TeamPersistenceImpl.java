package com.nischit.myexp.webflux.netty.persistence.sql;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nischit.myexp.webflux.domain.TeamDetails;
import com.nischit.myexp.webflux.netty.persistence.TeamPersistence;
import com.nischit.myexp.webflux.netty.persistence.sql.entity.TeamDetailsEntity;
import com.nischit.myexp.webflux.netty.persistence.sql.repository.TeamDetailsRepository;

import reactor.core.publisher.Mono;

@Repository
public class TeamPersistenceImpl implements TeamPersistence {

    private final TeamDetailsRepository teamDetailsRepository;

    @Autowired
    public TeamPersistenceImpl(final TeamDetailsRepository teamDetailsRepository) {
        this.teamDetailsRepository = teamDetailsRepository;
    }

    @Override
    public Mono<TeamDetails> createTeam(final TeamDetails teamDetails) {
        final TeamDetailsEntity teamDetailsEntity = TeamDetailsEntity.builder()
                .teamId(teamDetails.getTeamId())
                .teamName(teamDetails.getTeamName())
                .teamDesc(teamDetails.getTeamDesc())
                .build();
        // Dont make blocking call as below. This is just a demo
        return Mono.just(teamDetailsRepository.save(teamDetailsEntity)).map(entity -> teamDetails);
    }

    @Override
    public Mono<Optional<TeamDetails>> getTeamInfo(final String teamId) {
        final Mono<Optional<TeamDetailsEntity>> teamEntity = Mono.just(teamDetailsRepository.findById(teamId));
        return teamEntity.map(teamDetailsEntity -> teamDetailsEntity.map(entity -> Optional.of(TeamDetails.builder()
                .teamId(entity.getTeamId())
                .teamName(entity.getTeamName())
                .teamDesc(entity.getTeamDesc())
                .build()))
                .orElse(Optional.empty()));
    }

    @Override
    public Mono<Boolean> deleteTeam(final String teamId) {
        // Dont make blocking call as below. This is just a demo
        return Mono.fromSupplier(() ->  {
            teamDetailsRepository.deleteById(teamId);
            return Boolean.TRUE;
        });
    }
}
