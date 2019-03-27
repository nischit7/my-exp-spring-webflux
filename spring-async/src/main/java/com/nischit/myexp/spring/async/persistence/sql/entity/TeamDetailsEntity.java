package com.nischit.myexp.spring.async.persistence.sql.entity;

import com.google.common.base.Preconditions;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TeamDetailsEntity {

    @Id
    private String teamId;

    @Column(nullable = false)
    private String teamName;

    @Column(length = 4000)
    private String teamDesc;

    public TeamDetailsEntity() {
        // Nothing to do
    }

    public TeamDetailsEntity(final Builder builder) {
        Preconditions.checkArgument(StringUtils.hasText(builder.teamId), "Team Id cannot be null");
        Preconditions.checkArgument(StringUtils.hasText(builder.teamName), "Team Name cannot be null");
        this.teamId = builder.teamId;
        this.teamName = builder.teamName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDesc() {
        return teamDesc;
    }

    public void setTeamDesc(String teamDesc) {
        this.teamDesc = teamDesc;
    }

    public static final class Builder {

        private String teamId;

        private String teamName;

        private String teamDesc;

        public TeamDetailsEntity.Builder teamId(String teamId) {
            this.teamId = teamId;
            return this;
        }

        public TeamDetailsEntity.Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public TeamDetailsEntity.Builder teamDesc(String teamDesc) {
            this.teamDesc = teamDesc;
            return this;
        }

        public TeamDetailsEntity build() {
            return new TeamDetailsEntity(this);
        }
    }
}
