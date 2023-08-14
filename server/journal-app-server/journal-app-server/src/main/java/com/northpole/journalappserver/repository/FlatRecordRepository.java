package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.JournalEntryRecordDataSet;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FlatRecordRepository extends MongoRepository<FlatRecord, UUID> {

    @Query(value="{'journal':?0, 'topic':?1, 'recKey':?2}")
    public List<FlatRecord> findAllByIndices(Integer journal, String topic, String recKey);

    @Aggregation(
            pipeline= { "  {\n" +
                    "    $match:\n" +
                    "      {\n" +
                    "        journal: ?0,\n" +
                    "        topic: ?1,\n" +
                    "        recKey: ?2,\n" +
                    "      },\n" +
                    "  }",
                    "  {\n" +
                            "    $project: {_id:0,dateOfEntry:1,recKey:1,recValue:1}\n" +
                            "  }",
                    "  {\n" +
                            "    $facet: {\n" +
                            "      x: [\n" +
                            "        {\n" +
                            "          $project:{recKey:0, recValue:0}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $sort: {dateOfEntry:1}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $project: {\n" +
                            "            dateOfEntry:{\n" +
                            "              $dateToString: {\n" +
                            "                date:\"$dateOfEntry\"\n" +
                            "              }\n" +
                            "            },\n" +
                            "            recValue:{\n" +
                            "              $dateToString: {\n" +
                            "                date:\"$dateOfEntry\"\n" +
                            "              }\n" +
                            "            }\n" +
                            "            \n" +
                            "          }\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      y: [\n" +
                            "        {\n" +
                            "          $match: {recKey: \"a\"}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $project:{recKey:0}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $sort: {dateOfEntry:1}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $project:{\n" +
                            "            dateOfEntry: {\n" +
                            "              $dateToString: {\n" +
                            "                date:\"$dateOfEntry\"\n" +
                            "              }\n" +
                            "            },\n" +
                            "            recValue:1\n" +
                            "          }\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  }"
            }
    )
    public AggregationResults<JournalEntryRecordDataSet> getDataByDateOfEntry(Integer journal, String topic, String recKeyY);





    @Aggregation(
            pipeline= { "{\n" +
                    "    $match: {\n" +
                    "      journal: ?0,\n" +
                    "      topic: ?1,\n" +
                    "      recKey: {\n" +
                    "        $in: [?2, ?3],\n" +
                    "      },\n" +
                    "    },\n" +
                    "  }",
                    "  {\n" +
                            "    $project: {_id:0,dateOfEntry:1,recKey:1,recValue:1}\n" +
                            "  }",
                    "  {\n" +
                            "    $facet: {\n" +
                            "      x: [\n" +
                            "        {\n" +
                            "          $match: {recKey: \"a\"}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $project:{recKey:0}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $sort: {dateOfEntry:1}\n" +
                            "        }\n" +
                            "      ],\n" +
                            "      y: [\n" +
                            "        {\n" +
                            "          $match: {recKey: \"targetA\"}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $project:{recKey:0}\n" +
                            "        },\n" +
                            "        {\n" +
                            "          $sort: {dateOfEntry:1}\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  }"
            }
    )
    public AggregationResults<JournalEntryRecordDataSet> getDataByCustomField(Integer journal, String topic, String recKeyX, String recKeyY);
}
