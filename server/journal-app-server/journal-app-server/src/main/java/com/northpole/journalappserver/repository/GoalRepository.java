package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface GoalRepository extends MongoRepository<Goal, UUID> {
    @Query("{journal: ?0}")
    List<Goal> findAllByJournal(UUID journalRef);

    @Query("{journal: ?0, status:?1}")
    List<Goal> findByStatusInJournal(UUID journalRef, String status);

}
