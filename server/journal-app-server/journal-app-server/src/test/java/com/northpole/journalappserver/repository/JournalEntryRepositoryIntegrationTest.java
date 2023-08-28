package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.JournalBodyItem;
import com.northpole.journalappserver.entity.JournalEntry;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class JournalEntryRepositoryIntegrationTest {

    @Container
    private static MongoDBContainer mongodb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    private JournalEntryRepository journalEntryRepository;

    private JournalEntry mockJournalEntry;

    @Autowired
    public JournalEntryRepositoryIntegrationTest(JournalEntryRepository journalEntryRepository){
        this.journalEntryRepository = journalEntryRepository;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri",mongodb::getReplicaSetUrl);
    }



    @BeforeEach
    public void setupEachTest (){
        Record rec1 = Record.builder()
                .recKey("a")
                .recValue("1")
                .build();

        Record rec2 = Record.builder()
                .recKey("targetA")
                .recValue("2")
                .build();
        List<Record> recList1 = new ArrayList<>();
        recList1.add(rec1);
        recList1.add(rec2);

        JournalBodyItem body1 = JournalBodyItem.builder()
                .topic("My first topic")
                .description("My first topic ever!!!")
                .recordList(recList1)
                .build();
        JournalBodyItem body2 = JournalBodyItem.builder()
                .topic("Second")
                .description("This is my second topic...")
                .recordList(new ArrayList<Record>())
                .build();

        List<JournalBodyItem> bodyList = new ArrayList<>();
        bodyList.add(body1);
        bodyList.add(body2);

        this.mockJournalEntry = JournalEntry.builder()
                .entryId(UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7b3e5c8"))
                .journal(3)
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
                .journalBodyItems(bodyList)
                .build();



        List<JournalEntry> mockExistingJournalEntries = new ArrayList<>();

        mockExistingJournalEntries.add(
                JournalEntry.builder()
                        .entryId(UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7b3a1b2"))
                        .journal(1)
                        .summary("Dummy Entry")
                        .overview("Test1")
                        .dateOfEntry(LocalDateTime.of(2023,1,20,0,0,0,0))
                        .journalBodyItems(bodyList)
                        .build()
        );

        mockExistingJournalEntries.add(
                JournalEntry.builder()
                        .entryId(UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7123456"))
                        .journal(2)
                        .summary("Dummy Entry 2")
                        .overview("Test1")
                        .dateOfEntry(LocalDateTime.of(2023,3,31,0,0,0,0))
                        .journalBodyItems(bodyList)
                        .build()
        );

        mockExistingJournalEntries.add(
                JournalEntry.builder()
                        .entryId(UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7123456"))
                        .journal(3)
                        .summary("Dummy Entry 3")
                        .overview("This one should be picked up by findAllByJournal!")
                        .dateOfEntry(LocalDateTime.of(2023,6,30,0,0,0,0))
                        .journalBodyItems(bodyList)
                        .build()
        );

        mockExistingJournalEntries.add(
                JournalEntry.builder()
                        .entryId(UUID.fromString("7aa881bc-f6e1-4621-9325-c199d71a3b5c"))
                        .journal(3)
                        .summary("Dummy Entry 4")
                        .overview("This one should also be picked up by findAllByJournal, and this is the last entry!")
                        .dateOfEntry(LocalDateTime.of(2023,7,1,0,0,0,0))
                        .journalBodyItems(bodyList)
                        .build()
        );

        journalEntryRepository.saveAll(mockExistingJournalEntries);
    }

    @AfterEach
    public void cleanUpAfterEachTest(){
        journalEntryRepository.delete(mockJournalEntry);
    }



    @Test
    @DisplayName("Should save to data source")
    public void saveSuccess_IntegrationTest(){
        journalEntryRepository.save(mockJournalEntry);
        Optional<JournalEntry> searchResult = journalEntryRepository.findById(mockJournalEntry.getEntryId());
        assertNotEquals(Optional.empty(),searchResult);
    }

    @Test
    @DisplayName("Should return all journal entries with a given journalId")
    public void findAllByJournal_IntegrationTest(){
        int journalId=3;
        AggregationResults<JournalEntry> result = journalEntryRepository.findAllByJournal(journalId);

        List<JournalEntry> resultList = result.getMappedResults();

        assertEquals(2,resultList.size());
        assertEquals("This one should also be picked up by findAllByJournal, and this is the last entry!",resultList.get(0).getOverview());
        assertEquals("This one should be picked up by findAllByJournal!",resultList.get(1).getOverview());
    }

    @Test
    @DisplayName("Should return last journal entry with given journalId")
    public void findLastEntryInJournal_IntegrationTest(){
        int journalId=3;
        AggregationResults<JournalEntry>result = journalEntryRepository.findLastEntryInJournal(journalId);

        List<JournalEntry> resultList = result.getMappedResults();

        assertEquals(1,resultList.size());
        assertEquals("This one should also be picked up by findAllByJournal, and this is the last entry!",resultList.get(0).getOverview());
    }
}
