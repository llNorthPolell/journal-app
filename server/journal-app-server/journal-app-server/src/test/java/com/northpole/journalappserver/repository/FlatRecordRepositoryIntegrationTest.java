package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class FlatRecordRepositoryIntegrationTest {

    @Container
    private static MongoDBContainer mongodb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    private FlatRecordRepository flatRecordRepository;

    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");
    private final UUID MOCK_JOURNAL_ENTRY_ID=UUID.fromString("6d377a43-7100-4049-8e22-45055d09a869");
    private final UUID MOCK_JOURNAL_ENTRY_ID2=UUID.fromString("08cfe10d-4ddb-4837-b1d4-a11e01ad36c6");
    private final UUID MOCK_JOURNAL_ENTRY_ID3=UUID.fromString("44ff6a73-bafe-4943-a526-d0871715b310");

    private final UUID MOCK_DELETE_JOURNAL_ENTRY_ID=UUID.fromString("17121fe0-43ff-4695-8d0b-918210b15140");

    private List<FlatRecord> existingFlatRecordList;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        existingFlatRecordList=new ArrayList<>();

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_JOURNAL_ENTRY_ID2)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("3")
                        .dateOfEntry(LocalDateTime.parse("2022-08-20T04:00:00.000+00:00",formatter))
                        .build()
        );

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_JOURNAL_ENTRY_ID3)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                        .build()
        );

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_JOURNAL_ENTRY_ID3)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                        .build()
        );

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_JOURNAL_ENTRY_ID)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("2")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );


        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_JOURNAL_ENTRY_ID)
                        .topic("Second")
                        .recKey("b")
                        .recValue("asdfasdf")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_JOURNAL_ENTRY_ID)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("1")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_JOURNAL_ENTRY_ID)
                        .topic("Second")
                        .recKey("d")
                        .recValue("3")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_JOURNAL_ENTRY_ID)
                        .topic("Second")
                        .recKey("c")
                        .recValue("5")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter))
                        .build()
        );

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_DELETE_JOURNAL_ENTRY_ID)
                        .topic("DELETE")
                        .recKey("deleted")
                        .recValue("0")
                        .dateOfEntry(LocalDateTime.parse("2022-08-22T04:00:00.000+00:00",formatter))
                        .build()
        );

        existingFlatRecordList.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .journalEntry(MOCK_DELETE_JOURNAL_ENTRY_ID)
                        .topic("DELETE")
                        .recKey("deleted this too")
                        .recValue("1")
                        .dateOfEntry(LocalDateTime.parse("2022-08-22T04:00:00.000+00:00",formatter))
                        .build()
        );
        flatRecordRepository.saveAll(existingFlatRecordList);

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
        expected.add("0");
        expected.add("1");
        expected.add("6");
        expected.add("6");
        expected.add("3");
        expected.add("1");
        expected.add("2");
        expected.add("asdfasdf");
        expected.add("5");
        expected.add("3");

        List<String> actual = resultList.stream()
                .map(r->r.getRecValue())
                .collect(Collectors.toList());

        assertEquals(expected.size(),actual.size());

        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i),actual.get(i));

    }

    @Test
    @DisplayName("Should return list of flat records related to journal entry")
    public void findByJournalEntryId_IntegrationTest(){
        List<FlatRecord> result = flatRecordRepository.findAllByJournalEntry(MOCK_JOURNAL_ENTRY_ID2);

        assertNotNull(result);
        assertEquals(1,result.size());

        FlatRecord sampleResult = result.get(0);
        assertEquals("My first topic",sampleResult.getTopic());
        assertEquals("targetA",sampleResult.getRecKey());
        assertEquals("3",sampleResult.getRecValue());

    }


    @Test
    @DisplayName("Should delete the specified existing record")
    public void deleteFlatRecordsByJournalEntry_IntegrationTest(){
        final long count = flatRecordRepository.count();
        List<FlatRecord> recordsToDelete = flatRecordRepository.findAllByJournalEntry(MOCK_DELETE_JOURNAL_ENTRY_ID);

        assertNotNull(recordsToDelete);
        assertEquals(2,recordsToDelete.size());

        flatRecordRepository.deleteAll(recordsToDelete);

        long newCount=flatRecordRepository.count();
        assertEquals(count-2,newCount);

        List<FlatRecord> deletedRecords = flatRecordRepository.findAllByJournalEntry(MOCK_DELETE_JOURNAL_ENTRY_ID);
        assertTrue(deletedRecords.isEmpty());
    }

    @Test
    @DisplayName("Should return list of topic and recKey pairs under a specified journal")
    public void findRecordKeysInJournal_IntegrationTest(){
        List<RecordKey> recordKeys = flatRecordRepository.findRecordKeysInJournal(MOCK_JOURNAL_REF);

        assertNotNull(recordKeys);
        assertEquals(7,recordKeys.size());

        Set<RecordKey> expectedKeys = new HashSet<>();
        expectedKeys.add(RecordKey.builder().topic("My first topic").recKey("targetA").build());
        expectedKeys.add(RecordKey.builder().topic("My first topic").recKey("a").build());
        expectedKeys.add(RecordKey.builder().topic("Second").recKey("b").build());
        expectedKeys.add(RecordKey.builder().topic("Second").recKey("d").build());
        expectedKeys.add(RecordKey.builder().topic("Second").recKey("c").build());
        expectedKeys.add(RecordKey.builder().topic("DELETE").recKey("deleted").build());
        expectedKeys.add(RecordKey.builder().topic("DELETE").recKey("deleted this too").build());

        for (RecordKey r : recordKeys)
            assertTrue(expectedKeys.contains(r));
    }
}
