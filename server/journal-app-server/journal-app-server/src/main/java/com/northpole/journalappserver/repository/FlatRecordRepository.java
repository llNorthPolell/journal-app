package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.FlatRecord;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

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
    AggregationResults<FlatRecord> getDashboardData(Integer journal);


    @Query(value = "{'journal':?0, 'topic':?1, 'recKey':?2}")
    List<FlatRecord> findAllByIndices(Integer journal, String topic, String recKey);
}