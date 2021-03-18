package com.nischit.myexp.spring.async.persistence.sql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nischit.myexp.spring.async.persistence.sql.entity.TeamDetailsEntity;

/**
 * Team repository.
 */
@Repository
public interface TeamDetailsRepository extends CrudRepository<TeamDetailsEntity, String> {
}
