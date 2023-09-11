package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.RecordKey;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface FlatRecordRepository extends MongoRepository<FlatRecord, UUID> {
    @Aggregation(
            pipeline = {
                    """
                                                                        
                                    {
                                       $match: {
                                         journal: ?0,
                                       }
                                    }
                            """,
                    """
                                    {
                                       $project: {
                                         dateOfEntry: 1, topic: 1,recKey: 1,recValue: 1
                                       }
                                    }
                            """,
                    """
                                    {
                                       $sort: {
                                         dateOfEntry: -1,
                                         topic: 1,
                                         recKey: 1
                                       }
                                    }        
                            """
            }
    )
    AggregationResults<FlatRecord> getDashboardData(UUID journal);


    @Query(value = "{'journal':?0, 'topic':?1, 'recKey':?2}")
    List<FlatRecord> findAllByIndices(UUID journal, String topic, String recKey);


    @Query(value = "{'journalEntry':?0}")
    List<FlatRecord> findAllByJournalEntry(UUID journalEntry);


    @Aggregation(
            pipeline = {
                    """
                            {
                               $match: {journal:?0}
                            }
                            """,
                    """
                            {
                                $group:{_id:{topic: "$topic", recKey: "$recKey"}}
                            }
                            """,
                    """
                            {
                                $project: {_id:0,topic:"$_id.topic",recKey:"$_id.recKey"}
                            }
                            
                            """
            }
    )
    List<RecordKey> findRecordKeysInJournal(UUID journalRef);
}