package com.nischit.myexp.spring.async.persistence.sql.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.nischit.myexp.webflux.util.ValidationUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity object for teams.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TeamDetailsEntity {

    @Id
    @NotBlank
    private String teamId;

    @Column(nullable = false)
    @NotBlank
    private String teamName;

    @Column(length = 4000)
    @NotBlank
    private String teamDesc;

    /**
     * Creating a custom builder.
     *
     * @return new custom builder.
     */
    public static TeamDetailsEntity.TeamDetailsEntityBuilderCustom builder() {
        return new TeamDetailsEntity.TeamDetailsEntityBuilderCustom();
    }

    /**
     * Make sure the object is validated upon creation using builder.
     */
    public static class TeamDetailsEntityBuilderCustom extends TeamDetailsEntity.TeamDetailsEntityBuilder {

        @Override
        public TeamDetailsEntity build() {
            final TeamDetailsEntity obj = super.build();
            ValidationUtils.validate(obj);
            return obj;
        }
    }
}
