package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.*;
import com.northpole.journalappserver.entity.Record;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class FlatRecordRepositoryIntegrationTest {

    @Container
    private static MongoDBContainer mongodb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    private FlatRecordRepository flatRecordRepository;

    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");

    @Autowired
    public FlatRecordRepositoryIntegrationTest (
            FlatRecordRepository flatRecordRepository
    ){
        this.flatRecordRepository = flatRecordRepository;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri",mongodb::getReplicaSetUrl);
    }

    @BeforeEach
    public void setupBeforeEachTest(){
        List<FlatRecord> flatRecordList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("3")
                        .dateOfEntry(LocalDateTime.parse("2022-08-20T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("2")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );


        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("Second")
                        .recKey("b")
                        .recValue("asdfasdf")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("1")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("Second")
                        .recKey("d")
                        .recValue("3")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("Second")
                        .recKey("c")
                        .recValue("5")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordRepository.saveAll(flatRecordList);

    }

    @AfterEach
    public void cleanupAfterEachTest(){
        flatRecordRepository.deleteAll();
    }


    @Test
    @DisplayName("Should return dashboard data from record data in journal entries sorted by dateOfEntry (desc), topic, and recKey")
    public void getDashboardRecordData_IntegrationTest(){
        AggregationResults<FlatRecord> result = flatRecordRepository.getDashboardData(MOCK_JOURNAL_REF);

        assertNotNull(result);

        List<FlatRecord> resultList = result.getMappedResults();

        List<String> expected = new ArrayList<>();
        expected.add("6");
        expected.add("6");
        expected.add("3");
        expected.add("1");
        expected.add("2");
        expected.add("asdfasdf");
        expected.add("5");
        expected.add("3");

        List<String> actual = new ArrayList<>();

        for (FlatRecord r : resultList)
            actual.add(r.getRecValue());

        assertEquals(expected.size(),actual.size());

        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i),actual.get(i));

    }
}
