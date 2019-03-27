package com.nischit.myexp.webflux.netty.persistence.sql;

import com.nischit.myexp.webflux.netty.persistence.TeamPersistence;
import com.nischit.myexp.webflux.domain.TeamDetails;
import com.nischit.myexp.webflux.netty.persistence.sql.entity.TeamDetailsEntity;
import com.nischit.myexp.webflux.netty.persistence.sql.repository.TeamDetailsRepository;
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
    public Mono<TeamDetails> createTeam(TeamDetails teamDetails) {
        final TeamDetailsEntity teamDetailsEntity = new TeamDetailsEntity.Builder()
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
        return teamEntity.map(teamDetailsEntity -> teamDetailsEntity.map(entity -> Optional.of(new TeamDetails.Builder()
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
