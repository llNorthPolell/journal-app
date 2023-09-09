package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.northpole.journalappserver.entity.JournalBodyItem;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.entity.Record;
import com.northpole.journalappserver.repository.JournalEntryRepository;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class JournalEntryServiceTest {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private JournalEntryRecordService journalEntryRecordService;

    @Mock
    private GoalTrackerService goalTrackerService;

    @InjectMocks
    private JournalEntryService journalEntryService;

    private JournalEntry mockJournalEntry;

    private List<JournalEntry> mockJournalEntryResultList;

    private final UUID MOCK_NEW_JOURNAL_ENTRY_ID = UUID.fromString("377a51fb-4ef8-4e0e-a423-98d4018a44b9");
    private final UUID MOCK_JOURNAL_REF = UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");
    private final UUID MOCK_JOURNAL_REF2 = UUID.fromString("02cde6f9-a229-4f6e-9ff4-e156630909cf");
    private final UUID MOCK_NOT_OWNED_JOURNAL_ENTRY_ID = UUID.fromString("418b0f55-d719-49b1-bdd5-bfc657596b18");
    private final UUID MOCK_NOT_EXIST_JOURNAL_ENTRY_ID = UUID.fromString("de5afde6-7005-4cd8-8567-705cb430e662");

    @Autowired
    public JournalEntryServiceTest(
            JournalEntryService journalEntryService,
            JournalEntryRecordService journalEntryRecordService,
            GoalTrackerService goalTrackerService,
            JournalEntryRepository journalEntryRepository) {
        this.journalEntryService = journalEntryService;
        this.journalEntryRepository = journalEntryRepository;
        this.goalTrackerService = goalTrackerService;
        this.journalEntryRecordService = journalEntryRecordService;
    }

    @BeforeEach
    public void setupEachTest() throws JsonProcessingException {
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
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022, 8, 19, 0, 0, 0, 0))
                .journalBodyItems(bodyList)
                .build();

        this.mockJournalEntryResultList = new ArrayList<>();
        this.mockJournalEntryResultList.add(mockJournalEntry);

        when(journalEntryRepository.save(any(JournalEntry.class)))
                .thenReturn(JournalEntry.builder()
                        .entryId(MOCK_NEW_JOURNAL_ENTRY_ID)
                        .journal(MOCK_JOURNAL_REF)
                        .build());

        when(journalEntryRepository.findAllByJournal(any(UUID.class)))
                .thenReturn(new AggregationResults<>(mockJournalEntryResultList, new Document()));

        when(journalEntryRepository.findById(MOCK_NEW_JOURNAL_ENTRY_ID))
                .thenReturn(
                        Optional.of(
                                JournalEntry.builder()
                                        .entryId(MOCK_NEW_JOURNAL_ENTRY_ID)
                                        .journal(MOCK_JOURNAL_REF)
                                        .build()
                        )
                );

        when(journalEntryRepository.findById(MOCK_NOT_OWNED_JOURNAL_ENTRY_ID))
                .thenReturn(
                        Optional.of(
                                JournalEntry.builder()
                                        .entryId(MOCK_NOT_OWNED_JOURNAL_ENTRY_ID)
                                        .journal(MOCK_JOURNAL_REF2)
                                        .build()
                        )
                );

        when(journalEntryRepository.findLastEntryInJournal(any(UUID.class)))
                .thenReturn(Optional.of(new JournalEntry()));

        ReflectionTestUtils.setField(journalEntryService, "journalEntryRepository", journalEntryRepository);
        ReflectionTestUtils.setField(journalEntryService, "journalEntryRecordService", journalEntryRecordService);
        ReflectionTestUtils.setField(journalEntryService, "goalTrackerService", goalTrackerService);
    }


    @Test
    @DisplayName("Should call journalEntryRepository.save, journalEntryRecordService.save, and goalTrackerService.updateProgress, and return saved entry")
    public void saveSuccess_UnitTest() throws JsonProcessingException {
        when(journalEntryRecordService.save(any()))
                .thenReturn(new ArrayList<>());

        when(goalTrackerService.updateProgress(any(List.class)))
                .thenReturn("{\"objectives\": \"List of updated objectives...\"}");

        JournalEntry result = journalEntryService.publishJournalEntry(MOCK_JOURNAL_REF, mockJournalEntry);

        verify(journalEntryRepository, times(1)).save(any());
        verify(journalEntryRecordService, times(1)).save(any());
        verify(goalTrackerService, times(1)).updateProgress(any());

    }

    @Test
    @DisplayName("Should return null when journalEntryRecordService.save fails")
    public void journalEntryRecordServiceFails_UnitTest() throws JsonProcessingException {
        when(journalEntryRecordService.save(any()))
                .thenReturn(new ArrayList<>());

        when(goalTrackerService.updateProgress(any()))
                .thenReturn("{\"objectives\": \"List of updated objectives...\"}");

        JournalEntry result = journalEntryService.publishJournalEntry(MOCK_JOURNAL_REF, mockJournalEntry);

        verify(journalEntryRepository, times(1)).save(any());
        verify(journalEntryRecordService, times(1)).save(any());
        verify(goalTrackerService, times(1)).updateProgress(any());

        assertNull(result);
    }

    @Test
    @DisplayName("Should return null when goalTrackerService.updateProgress fails")
    public void goalTrackerServiceFails_UnitTest() throws JsonProcessingException {
        when(journalEntryRecordService.save(any()))
                .thenReturn(new ArrayList<>());

        when(goalTrackerService.updateProgress(any()))
                .thenReturn(null);

        JournalEntry result = journalEntryService.publishJournalEntry(MOCK_JOURNAL_REF, mockJournalEntry);

        verify(journalEntryRepository, times(1)).save(any());
        verify(journalEntryRecordService, times(1)).save(any());
        verify(goalTrackerService, times(1)).updateProgress(any());

        assertNull(result);
    }

    @Test
    @DisplayName("Should call JournalEntryRepository.findById once to return journal entry for given entryId")
    public void getJournalEntryById_UnitTest() {
        Optional<JournalEntry> result = journalEntryRepository.findById(MOCK_JOURNAL_REF);
        verify(journalEntryRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should call JournalEntryRepository.findAllByJournal once to return all journal entries in a journal")
    public void getJournalEntriesInJournal_UnitTest() {
        List<JournalEntry> result = journalEntryService.getJournalEntriesInJournal(MOCK_JOURNAL_REF);

        verify(journalEntryRepository, times(1)).findAllByJournal(any(UUID.class));
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should call JournalEntryRepository.findLastEntryInJournal once to return the last journal entry in a journal")
    public void getLastEntryInJournal_UnitTest() {
        JournalEntry result = journalEntryService.getLastEntryInJournal(MOCK_JOURNAL_REF);
        verify(journalEntryRepository, times(1)).findLastEntryInJournal(any(UUID.class));
    }

    @Test
    @DisplayName("Should call journalEntryRepository.findById and journalEntryRepository.save once when trying to update entry")
    public void updateJournalEntry_UnitTest() throws JsonProcessingException {
        JournalEntry result = journalEntryService.updateJournalEntry(MOCK_NEW_JOURNAL_ENTRY_ID, new JournalEntry());
        verify(journalEntryRepository, times(1)).findById(any(UUID.class));
        verify(journalEntryRepository, times(1)).save(any(JournalEntry.class));
    }

    @Test
    @DisplayName("Should call journalEntryRepository.findById and journalEntryRepository.delete once when trying to delete an entry")
    public void deleteJournalEntry_UnitTest() throws JsonProcessingException {
        JournalEntry result = journalEntryService.deleteJournalEntry(MOCK_NEW_JOURNAL_ENTRY_ID);
        verify(journalEntryRepository, times(1)).findById(any(UUID.class));
        verify(journalEntryRepository, times(1)).delete(any(JournalEntry.class));
    }


    @Test
    @DisplayName("Should return true if journal entry is part of journal in ownsJournal check")
    public void ownsJournal_success_UnitTest(){
        boolean result =journalEntryService.ownsJournalEntry(MOCK_JOURNAL_REF,MOCK_NEW_JOURNAL_ENTRY_ID);
        verify(journalEntryRepository, times(1)).findById(any(UUID.class));
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false if journal entry is not part of journal in ownsJournal check")
    public void ownsJournal_notOwned_UnitTest(){
        boolean result =journalEntryService.ownsJournalEntry(MOCK_JOURNAL_REF,MOCK_NOT_OWNED_JOURNAL_ENTRY_ID);
        verify(journalEntryRepository, times(1)).findById(any(UUID.class));
        assertTrue(!result);
    }

    @Test
    @DisplayName("Should return false if journal entry does not exist in ownsJournal check")
    public void ownsJournal_notExist_UnitTest(){
        boolean result =journalEntryService.ownsJournalEntry(MOCK_JOURNAL_REF,MOCK_NOT_EXIST_JOURNAL_ENTRY_ID);
        verify(journalEntryRepository, times(1)).findById(any(UUID.class));
        assertTrue(!result);
    }
}
