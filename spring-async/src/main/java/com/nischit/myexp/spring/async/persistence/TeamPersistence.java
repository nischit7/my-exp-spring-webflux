package com.nischit.myexp.spring.async.persistence;

import java.util.Optional;

import com.nischit.myexp.webflux.domain.TeamDetails;

/**
 * Persistence layer to save team information.
 */
public interface TeamPersistence {

    /**
     * Creates team.
     *
     * @param teamDetails An instance of {@link TeamDetails}.
     * @return An instance of {@link TeamDetails}.
     */
    TeamDetails createTeam(TeamDetails teamDetails);

    /**
     * Get team info.
     *
     * @param teamId Team id
     * @return An instance of {@link Optional}.
     */
    Optional<TeamDetails> getTeamInfo(String teamId);

    /**
     * Deletes the team.
     *
     * @param teamId Team id
     * @return Returns {@code true} if successful.
     */
    boolean deleteTeam(String teamId);
}
