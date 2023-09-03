package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalBodyItem;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.entity.Record;
import com.northpole.journalappserver.repository.FlatRecordRepository;
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
                .journal(3)
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
                .journalBodyItems(bodyList)
                .build();

        this.mockJournalEntryResultList = new ArrayList<>();
        this.mockJournalEntryResultList.add(mockJournalEntry);

        when(journalEntryRepository.save(any(JournalEntry.class)))
                .thenReturn(this.mockJournalEntry);

        when(journalEntryRepository.findAllByJournal(anyInt()))
                .thenReturn(new AggregationResults<>(mockJournalEntryResultList, new Document()));

        when(journalEntryRecordService.save(any(JournalEntry.class)))
                .thenReturn(GeneralResponseBody.builder()
                        .status(200)
                        .message("{\"Content\":\"List of flat records.... \"}")
                        .timeStamp(System.currentTimeMillis())
                        .build());

        ReflectionTestUtils.setField(journalEntryService,"journalEntryRepository", journalEntryRepository);
        ReflectionTestUtils.setField(journalEntryService,"journalEntryRecordService", journalEntryRecordService);
        ReflectionTestUtils.setField(journalEntryService,"goalTrackerService", goalTrackerService);
    }

    @Test
    @DisplayName("Should pass journal entry JSON to DAO to save once")
    public void saveSuccess_UnitTest(){
        GeneralResponseBody result = journalEntryService.save(mockJournalEntry);

        assertEquals(200,result.getStatus());
        verify(journalEntryRepository,times(1)).save(any(JournalEntry.class));
        verify(journalEntryRecordService,times(1)).save(any(JournalEntry.class));
        verify(goalTrackerService,times(1)).updateProgress(anyString());
    }


    @Test
    @DisplayName("Should call JournalEntryRepository.findAllByJournal() once to return all journal entries for a given journalId")
    public void getJournalEntriesById_UnitTest(){
        int journalId=3;
        List<JournalEntry> result = journalEntryService.getJournalEntriesById(journalId);

        verify(journalEntryRepository,times(1)).findAllByJournal(anyInt());
        assertEquals(1,result.size());
    }


}
