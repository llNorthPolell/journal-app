package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface ObjectiveRepository extends JpaRepository<Objective, Integer> {
    List<Objective> findAllByJournalIdIsAndTopicInAndStatusIs(@Param("journalId") int journalId,
                                                            @Param("topics") Set<String> topics,
                                                            @Param("status") String status);

    List<Objective> findAllByJournalId(@Param("journalId") int journalId);

    List<Objective> findAllByGoalId(@Param("goalId") UUID goalId);
}
