package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.JournalEntry;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface JournalEntryRepository extends MongoRepository<JournalEntry, UUID> {
    @Aggregation(
            pipeline={
                    """
                            {
                                $match: {
                                    journal: ?0
                                }
                            }
                            """,
                    """
                            {
                                $sort: {
                                    dateOfEntry:-1
                                }                            
                            }
                            """
            }
    )
    AggregationResults<JournalEntry> findAllByJournal(UUID journalId);

    @Aggregation(
            pipeline={
                    """
                            {
                                $match: {
                                    journal: ?0
                                }
                            }
                            """,
                    """
                            {
                                $sort: {
                                    dateOfEntry:-1
                                }                            
                            }
                            """,
                    """
                            {
                                $limit : 1
                            }
                            """
            }
    )
    Optional<JournalEntry> findLastEntryInJournal(UUID journalId);
}
