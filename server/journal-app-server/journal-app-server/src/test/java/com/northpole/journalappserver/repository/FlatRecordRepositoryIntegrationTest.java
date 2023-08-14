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
                        .journal(3)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("3")
                        .dateOfEntry(LocalDateTime.parse("2022-08-20T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(3)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(3)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(3)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("2")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );


        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(3)
                        .topic("Second")
                        .recKey("b")
                        .recValue("asdfasdf")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(3)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("1")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(3)
                        .topic("Second")
                        .recKey("d")
                        .recValue("3")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        flatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(3)
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

    //TODO: Test case fails only with testcontainers as it compares *T04:00 to *T08:00 (looks like time zone was added)
    @Test
    @DisplayName("Should return expected JournalEntryRecordDataSet object with x as recKey=dateOfEntry and y as recKey=\"a\"")
    public void getDataByDateOfEntry_IntegrationTest(){
        int journal= 3;
        String topic = "My first topic";
        String recKey= "a";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        DateAndValue x1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19 04:00",formatter),
                "2022-08-19T04:00:00.000Z");


        DateAndValue x2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21 04:00",formatter),
                "2022-08-21T04:00:00.000Z");


        DateAndValue y1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19 04:00",formatter),
                "1");


        DateAndValue y2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21 04:00",formatter),
                "6");

        List<DateAndValue> x = new ArrayList();
        x.add(x1);
        x.add(x2);

        List<DateAndValue> y = new ArrayList<>();
        y.add(y1);
        y.add(y2);

        JournalEntryRecordDataSet expected = JournalEntryRecordDataSet.builder()
                .x(x)
                .y(y)
                .build();

        AggregationResults<JournalEntryRecordDataSet> result = flatRecordRepository.getDataByDateOfEntry(journal,topic,recKey);

        JournalEntryRecordDataSet actual = result.getMappedResults().get(0);


        List<FlatRecord> result2 = flatRecordRepository.findAll();

        assertNotNull(result);
        assertEquals(expected,actual);
    }




    @Test
    @DisplayName("Should return expected JournalEntryRecordDataSet object with x as recKey=\"a\" and y as recKey=\"targetA\"")
    public void getDataByCustomField_IntegrationTest(){
        int journal= 3;
        String topic = "My first topic";
        String recKeyX= "a";
        String recKeyY= "targetA";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        DateAndValue x1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19 04:00",formatter),
                "1");


        DateAndValue x2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21 04:00",formatter),
                "6");


        DateAndValue y1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19 04:00",formatter),
                "2");


        DateAndValue y2 = new DateAndValue(
                LocalDateTime.parse("2022-08-20 04:00",formatter),
                "3");

        DateAndValue y3 = new DateAndValue(
                LocalDateTime.parse("2022-08-21 04:00",formatter),
                "6");

        List<DateAndValue> x = new ArrayList();
        x.add(x1);
        x.add(x2);

        List<DateAndValue> y = new ArrayList<>();
        y.add(y1);
        y.add(y2);
        y.add(y3);

        JournalEntryRecordDataSet expected = JournalEntryRecordDataSet.builder()
                .x(x)
                .y(y)
                .build();

        AggregationResults<JournalEntryRecordDataSet> result = flatRecordRepository.getDataByCustomField(journal,topic,recKeyX,recKeyY);

        JournalEntryRecordDataSet actual = result.getMappedResults().get(0);

        assertNotNull(result);
        assertEquals(expected,actual);
    }
}
