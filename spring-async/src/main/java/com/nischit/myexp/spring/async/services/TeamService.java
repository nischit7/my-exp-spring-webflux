package com.nischit.myexp.spring.async.services;

import com.nischit.myexp.webflux.domain.TeamDetails;

/**
 * A service class that manages team information.
 */
public interface TeamService {

    /**
     * Creates team.
     *
     * @param teamDetails An instance of {@link TeamDetails}.
     * @return An instance of {@link TeamDetails}.
     */
    TeamDetails createTeam(TeamDetails teamDetails);

    /**
     * Gets the team details.
     *
     * @param teamId Team id.
     * @return An instance of {@link TeamDetails}.
     */
    TeamDetails getTeamInfo(String teamId);

    /**
     * Deletes the team.
     *
     * @param teamId Team id.
     * @return Returns {@code true} if successful.
     */
    boolean deleteTeam(String teamId);
}
