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

    private final UUID MOCK_NEW_JOURNAL_ENTRY_ID=UUID.fromString("377a51fb-4ef8-4e0e-a423-98d4018a44b9");
    private final UUID MOCK_JOURNAL_REF= UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");

    @Autowired
    public JournalEntryServiceTest(
            JournalEntryService journalEntryService,
            JournalEntryRecordService journalEntryRecordService,
            GoalTrackerService goalTrackerService,
            JournalEntryRepository journalEntryRepository){
        this.journalEntryService=journalEntryService;
        this.journalEntryRepository=journalEntryRepository;
        this.goalTrackerService=goalTrackerService;
        this.journalEntryRecordService=journalEntryRecordService;
    }

    @BeforeEach
    public void setupEachTest () throws JsonProcessingException {
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
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
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

        ReflectionTestUtils.setField(journalEntryService,"journalEntryRepository", journalEntryRepository);
        ReflectionTestUtils.setField(journalEntryService,"journalEntryRecordService", journalEntryRecordService);
        ReflectionTestUtils.setField(journalEntryService,"goalTrackerService", goalTrackerService);
    }


    @Test
    @DisplayName("Should call journalEntryRepository.save, journalEntryRecordService.save, and goalTrackerService.updateProgress, and return saved entry")
    public void saveSuccess_UnitTest(){
        try {
            when(journalEntryRecordService.save(any()))
                    .thenReturn("{\"Content\":\"List of flat records.... \"}");

            when(goalTrackerService.updateProgress(anyString()))
                    .thenReturn("{\"objectives\": \"List of updated objectives...\"}");

            JournalEntry result = journalEntryService.save(MOCK_JOURNAL_REF, mockJournalEntry);

            verify(journalEntryRepository, times(1)).save(any());
            verify(journalEntryRecordService, times(1)).save(any());
            verify(goalTrackerService, times(1)).updateProgress(any());
            assertEquals(MOCK_JOURNAL_REF,result.getJournal());
            assertEquals(MOCK_NEW_JOURNAL_ENTRY_ID,result.getEntryId());
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should return null when journalEntryRecordService.save fails")
    public void journalEntryRecordServiceFails_UnitTest(){
        try {
            when(journalEntryRecordService.save(any()))
                    .thenReturn(null);

            when(goalTrackerService.updateProgress(any()))
                    .thenReturn("{\"objectives\": \"List of updated objectives...\"}");

            JournalEntry result = journalEntryService.save(MOCK_JOURNAL_REF, mockJournalEntry);

            verify(journalEntryRepository, times(1)).save(any());
            verify(journalEntryRecordService, times(1)).save(any());
            verify(goalTrackerService, times(1)).updateProgress(any());

            assertNull(result);
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should return null when goalTrackerService.updateProgress fails")
    public void goalTrackerServiceFails_UnitTest(){
        try {
            when(journalEntryRecordService.save(any()))
                    .thenReturn("{\"Content\":\"List of flat records.... \"}");

            when(goalTrackerService.updateProgress(any()))
                    .thenReturn(null);

            JournalEntry result = journalEntryService.save(MOCK_JOURNAL_REF, mockJournalEntry);

            verify(journalEntryRepository, times(1)).save(any());
            verify(journalEntryRecordService, times(1)).save(any());
            verify(goalTrackerService, times(1)).updateProgress(any());

            assertNull(result);
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }



    @Test
    @DisplayName("Should call JournalEntryRepository.findAllByJournal() once to return all journal entries for a given journalId")
    public void getJournalEntriesById_UnitTest(){
        List<JournalEntry> result = journalEntryService.getJournalEntriesInJournal(MOCK_JOURNAL_REF);

        verify(journalEntryRepository,times(1)).findAllByJournal(any(UUID.class));
        assertEquals(1,result.size());
    }


}
