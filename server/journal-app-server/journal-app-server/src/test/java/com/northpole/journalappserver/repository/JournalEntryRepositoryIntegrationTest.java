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
    private List<JournalEntry> mockExistingJournalEntries;

    private final UUID MOCK_NEW_JOURNAL_ENTRY_ID=UUID.fromString("8ee6d6e8-cd5f-43a6-870b-06745befc7e3");
    private final UUID MOCK_JOURNAL_REF=UUID.fromString("b4a2679a-bf33-4835-85c2-08c4e41c284b");
    private final UUID MOCK_JOURNAL_REF2=UUID.fromString("ab8526d6-ccb8-4cd1-a6ff-6a8108f1a703");
    private final UUID MOCK_JOURNAL_REF3=UUID.fromString("73dc2074-4be8-45a3-ac16-8031751ccb69");

    private final UUID MOCK_EXISTING_JOURNAL_ENTRY_REF1=UUID.fromString("d8c4c61e-7c8a-4077-a792-8b9527ea8cdd");
    private final UUID MOCK_EXISTING_JOURNAL_ENTRY_REF2=UUID.fromString("14aa722d-99bf-4cb6-b545-5287bd49eb63");
    private final UUID MOCK_EXISTING_JOURNAL_ENTRY_REF3=UUID.fromString("37b3dcde-5bad-4f85-ac84-5b78e66276af");
    private final UUID MOCK_EXISTING_JOURNAL_ENTRY_REF4=UUID.fromString("a9e5f048-2a05-4891-8097-9be1f7ac38a3");

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
        mockExistingJournalEntries = new ArrayList<>();

        mockExistingJournalEntries.add(
                JournalEntry.builder()
                        .entryId(MOCK_EXISTING_JOURNAL_ENTRY_REF1)
                        .journal(MOCK_JOURNAL_REF)
                        .summary("Dummy Entry")
                        .overview("Test1")
                        .dateOfEntry(LocalDateTime.of(2023,1,20,0,0,0,0))
                        .journalBodyItems(new ArrayList<>())
                        .build()
        );

        List<JournalBodyItem> existingBodyItems = new ArrayList<>();
        List<Record> existingRecord1=new ArrayList<>();
        List<Record> existingRecord2=new ArrayList<>();

        existingRecord1.add(
                Record.builder()
                        .recKey("a")
                        .recValue("1")
                        .build()
        );

        existingRecord1.add(
                Record.builder()
                        .recKey("b")
                        .recValue("2")
                        .build()
        );

        existingBodyItems.add(
                JournalBodyItem.builder()
                        .description("Test Body Item")
                        .topic("test")
                        .recordList(existingRecord1)
                        .build()

        );

        existingRecord2.add(
                Record.builder()
                        .recKey("c")
                        .recValue("1")
                        .build()
        );

        existingRecord2.add(
                Record.builder()
                        .recKey("d")
                        .recValue("2")
                        .build()
        );

        existingBodyItems.add(
                JournalBodyItem.builder()
                        .description("Test Body Item2")
                        .topic("test")
                        .recordList(existingRecord2)
                        .build()
        );

        mockExistingJournalEntries.add(
                JournalEntry.builder()
                        .entryId(MOCK_EXISTING_JOURNAL_ENTRY_REF2)
                        .journal(MOCK_JOURNAL_REF2)
                        .summary("Dummy Entry 2")
                        .overview("Test1")
                        .dateOfEntry(LocalDateTime.of(2023,3,31,0,0,0,0))
                        .journalBodyItems(existingBodyItems)
                        .build()
        );

        mockExistingJournalEntries.add(
                JournalEntry.builder()
                        .entryId(MOCK_EXISTING_JOURNAL_ENTRY_REF3)
                        .journal(MOCK_JOURNAL_REF3)
                        .summary("Dummy Entry 3")
                        .overview("This one should be picked up by findAllByJournal!")
                        .dateOfEntry(LocalDateTime.of(2023,6,30,0,0,0,0))
                        .journalBodyItems(new ArrayList<>())
                        .build()
        );

        mockExistingJournalEntries.add(
                JournalEntry.builder()
                        .entryId(MOCK_EXISTING_JOURNAL_ENTRY_REF4)
                        .journal(MOCK_JOURNAL_REF3)
                        .summary("Dummy Entry 4")
                        .overview("This one should also be picked up by findAllByJournal, and this is the last entry!")
                        .dateOfEntry(LocalDateTime.of(2023,7,1,0,0,0,0))
                        .journalBodyItems(new ArrayList<>())
                        .build()
        );

        journalEntryRepository.saveAll(mockExistingJournalEntries);
    }

    @AfterEach
    public void cleanupAfterEachTest(){
        journalEntryRepository.deleteAll(mockExistingJournalEntries);
    }

    @Test
    @DisplayName("Should save to data source")
    public void createSuccess_IntegrationTest(){
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

        mockJournalEntry = JournalEntry.builder()
                .entryId(MOCK_NEW_JOURNAL_ENTRY_ID)
                .journal(MOCK_JOURNAL_REF3)
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
                .journalBodyItems(bodyList)
                .build();



        JournalEntry saveResult = journalEntryRepository.save(mockJournalEntry);

        Optional<JournalEntry> findNewEntryResult = journalEntryRepository.findById(saveResult.getEntryId());
        assertTrue(findNewEntryResult.isPresent());

        JournalEntry newJournalEntry=findNewEntryResult.get();

        assertEquals(mockJournalEntry.getJournal(),newJournalEntry.getJournal());
        assertEquals(mockJournalEntry.getOverview(),newJournalEntry.getOverview());
        assertEquals(mockJournalEntry.getSummary(),newJournalEntry.getSummary());

        List<JournalBodyItem> mockBodyItems = mockJournalEntry.getJournalBodyItems();
        List<JournalBodyItem> newBodyItems = newJournalEntry.getJournalBodyItems();

        assertEquals(mockBodyItems.size(), newBodyItems.size());

        JournalBodyItem sampleMockBodyItem = mockBodyItems.get(0);
        JournalBodyItem sampleNewBodyItem = newBodyItems.get(0);

        assertEquals(sampleMockBodyItem.getTopic(),sampleNewBodyItem.getTopic());
        assertEquals(sampleMockBodyItem.getDescription(),sampleNewBodyItem.getDescription());

        List<Record> mockRecords = sampleMockBodyItem.getRecordList();
        List<Record> newRecords = sampleNewBodyItem.getRecordList();

        assertEquals(mockRecords.size(), newRecords.size());

        Record sampleMockRecord = mockRecords.get(0);
        Record sampleNewRecord = newRecords.get(0);

        assertEquals(sampleMockRecord.getRecKey(),sampleNewRecord.getRecKey());
        assertEquals(sampleMockRecord.getRecValue(),sampleNewRecord.getRecValue());

        journalEntryRepository.delete(mockJournalEntry);
    }

    @Test
    @DisplayName("Should return all journal entries with a given journalId")
    public void findAllByJournal_IntegrationTest(){
        AggregationResults<JournalEntry> result = journalEntryRepository.findAllByJournal(MOCK_JOURNAL_REF3);

        List<JournalEntry> resultList = result.getMappedResults();

        assertEquals(2,resultList.size());
        assertEquals("This one should also be picked up by findAllByJournal, and this is the last entry!",resultList.get(0).getOverview());
        assertEquals("This one should be picked up by findAllByJournal!",resultList.get(1).getOverview());
    }

    @Test
    @DisplayName("Should return last journal entry with given journalId")
    public void findLastEntryInJournal_IntegrationTest(){
        Optional<JournalEntry>findResult = journalEntryRepository.findLastEntryInJournal(MOCK_JOURNAL_REF3);

        JournalEntry result= findResult.get();

        assertEquals("This one should also be picked up by findAllByJournal, and this is the last entry!",result.getOverview());
    }

    @Test
    @DisplayName("Should update journal entry and replace Journal Entry Body with an empty arraylist")
    public void updateJournalEntry_replaceBody_IntegrationTest(){
        final LocalDateTime now = LocalDateTime.now();
        final long count = journalEntryRepository.count();

        final JournalEntry mockUpdatePayload = JournalEntry.builder()
                .summary("Updated summary")
                .overview("This journal entry has been updated and its body has been replaced.")
                .lastUpdated(now)
                .dateOfEntry(LocalDateTime.of(2023,9,8,12,0,0,0))
                .journalBodyItems(new ArrayList<>())
                .build();

        Optional<JournalEntry> findJournalEntryToUpdateResult = journalEntryRepository.findById(MOCK_EXISTING_JOURNAL_ENTRY_REF2);
        assertTrue(findJournalEntryToUpdateResult.isPresent());

        JournalEntry journalEntryToUpdate = findJournalEntryToUpdateResult.get();
        journalEntryToUpdate.setOverview(mockUpdatePayload.getOverview());
        journalEntryToUpdate.setSummary(mockUpdatePayload.getSummary());
        journalEntryToUpdate.setLastUpdated(mockUpdatePayload.getLastUpdated());
        journalEntryToUpdate.setDateOfEntry(mockUpdatePayload.getDateOfEntry());
        journalEntryToUpdate.setJournalBodyItems(mockUpdatePayload.getJournalBodyItems());

        JournalEntry result = journalEntryRepository.save(journalEntryToUpdate);

        assertNotNull(result);

        long newCount = journalEntryRepository.count();
        assertEquals(count,newCount);

        Optional<JournalEntry> findUpdatedJournalEntryResult = journalEntryRepository.findById(MOCK_EXISTING_JOURNAL_ENTRY_REF2);
        assertTrue(findUpdatedJournalEntryResult.isPresent());

        JournalEntry updatedJournalEntry = findUpdatedJournalEntryResult.get();

        assertEquals(MOCK_JOURNAL_REF2,updatedJournalEntry.getJournal());
        assertEquals(mockUpdatePayload.getSummary(),updatedJournalEntry.getSummary());
        assertEquals(mockUpdatePayload.getOverview(),updatedJournalEntry.getOverview());
        assertEquals(mockUpdatePayload.getDateOfEntry(),updatedJournalEntry.getDateOfEntry());
        assertTrue(updatedJournalEntry.getJournalBodyItems().isEmpty());
    }

    @Test
    @DisplayName("Should update journal entry and update fields in the Journal Entry Body")
    public void updateJournalEntry_modifyBody_IntegrationTest(){
        final LocalDateTime now = LocalDateTime.now();
        final long count = journalEntryRepository.count();

        Optional<JournalEntry> findJournalEntryToUpdateResult = journalEntryRepository.findById(MOCK_EXISTING_JOURNAL_ENTRY_REF2);
        assertTrue(findJournalEntryToUpdateResult.isPresent());

        JournalEntry journalEntryToUpdate = findJournalEntryToUpdateResult.get();

        List<JournalBodyItem> mockBodyItems = journalEntryToUpdate.getJournalBodyItems();
        mockBodyItems.remove(0);
        JournalBodyItem mockBodyItemPayload = mockBodyItems.get(0);
        mockBodyItemPayload.setDescription(mockBodyItemPayload.getDescription()+" - updated");

        List<Record> mockRecords = mockBodyItemPayload.getRecordList();
        mockRecords.remove(1);

        Record mockRecordPayload = mockRecords.get(0);
        mockRecordPayload.setRecKey("f");
        mockRecordPayload.setRecValue("4");

        final JournalEntry mockUpdatePayload = JournalEntry.builder()
                .summary("Updated summary")
                .overview("This journal entry has been updated and its body was also modified.")
                .lastUpdated(now)
                .dateOfEntry(LocalDateTime.of(2023,9,8,12,0,0,0))
                .journalBodyItems(mockBodyItems)
                .build();

        journalEntryToUpdate.setOverview(mockUpdatePayload.getOverview());
        journalEntryToUpdate.setSummary(mockUpdatePayload.getSummary());
        journalEntryToUpdate.setLastUpdated(mockUpdatePayload.getLastUpdated());
        journalEntryToUpdate.setDateOfEntry(mockUpdatePayload.getDateOfEntry());
        journalEntryToUpdate.setJournalBodyItems(mockUpdatePayload.getJournalBodyItems());

        JournalEntry result = journalEntryRepository.save(journalEntryToUpdate);

        assertNotNull(result);

        long newCount = journalEntryRepository.count();
        assertEquals(count,newCount);

        Optional<JournalEntry> findUpdatedJournalEntryResult = journalEntryRepository.findById(MOCK_EXISTING_JOURNAL_ENTRY_REF2);
        assertTrue(findUpdatedJournalEntryResult.isPresent());

        JournalEntry updatedJournalEntry = findUpdatedJournalEntryResult.get();

        assertEquals(MOCK_JOURNAL_REF2,updatedJournalEntry.getJournal());
        assertEquals(mockUpdatePayload.getSummary(),updatedJournalEntry.getSummary());
        assertEquals(mockUpdatePayload.getOverview(),updatedJournalEntry.getOverview());
        assertEquals(mockUpdatePayload.getDateOfEntry(),updatedJournalEntry.getDateOfEntry());

        List<JournalBodyItem> updatedBody=updatedJournalEntry.getJournalBodyItems();

        assertEquals(1,updatedBody.size());

        JournalBodyItem updatedBodyItem = updatedBody.get(0);

        assertEquals(mockBodyItemPayload.getDescription(),updatedBodyItem.getDescription());

        List<Record> updatedRecordList = updatedBodyItem.getRecordList();

        assertEquals(1,updatedRecordList.size());

        Record updatedRecord = updatedRecordList.get(0);

        assertEquals(mockRecordPayload.getRecKey(),updatedRecord.getRecKey());
        assertEquals(mockRecordPayload.getRecValue(),updatedRecord.getRecValue());

    }

    @Test
    @DisplayName("Should delete journal entry")
    public void deleteJournalEntry_IntegrationTest(){
        final long count = journalEntryRepository.count();
        Optional<JournalEntry> findJournalEntryToDeleteResult = journalEntryRepository.findById(MOCK_EXISTING_JOURNAL_ENTRY_REF2);
        assertTrue(findJournalEntryToDeleteResult.isPresent());

        JournalEntry journalEntryToDelete = findJournalEntryToDeleteResult.get();
        journalEntryRepository.delete(journalEntryToDelete);

        long newCount = journalEntryRepository.count();
        assertEquals(count-1,newCount);

        Optional<JournalEntry> deletedJournalEntry = journalEntryRepository.findById(MOCK_EXISTING_JOURNAL_ENTRY_REF2);
        assertTrue(deletedJournalEntry.isEmpty());

    }

}
