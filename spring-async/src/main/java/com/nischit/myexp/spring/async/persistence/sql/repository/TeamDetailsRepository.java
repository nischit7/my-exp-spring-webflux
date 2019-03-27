package com.nischit.myexp.spring.async.persistence.sql.repository;

import com.nischit.myexp.spring.async.persistence.sql.entity.TeamDetailsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamDetailsRepository extends CrudRepository<TeamDetailsEntity, String> {
}
