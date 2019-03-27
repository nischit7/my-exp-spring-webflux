package com.nischit.myexp.webflux.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.Preconditions;
import org.springframework.util.StringUtils;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
@JsonDeserialize(builder = TeamDetails.Builder.class)
public class TeamDetails implements Serializable {

    private String teamId;

    private String teamName;

    private String teamDesc;

    public TeamDetails(final Builder builder) {
        Preconditions.checkArgument(StringUtils.hasText(builder.teamId), "Team Id cannot be null");
        Preconditions.checkArgument(StringUtils.hasText(builder.teamName), "Team Name cannot be null");
        this.teamId = builder.teamId;
        this.teamName = builder.teamName;
        this.teamDesc = builder.teamDesc;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamDesc() {
        return teamDesc;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {

        String teamId;

        String teamName;

        String teamDesc;

        public Builder teamId(String teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder teamDesc(String teamDesc) {
            this.teamDesc = teamDesc;
            return this;
        }

        public TeamDetails build() {
            return new TeamDetails(this);
        }
    }
}
