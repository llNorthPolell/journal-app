package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalBodyItem;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.entity.Record;
import com.northpole.journalappserver.repository.FlatRecordRepository;
import com.northpole.journalappserver.repository.JournalEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @InjectMocks
    private JournalEntryService journalEntryService;

    private JournalEntry mockJournalEntry;

    @Autowired
    public JournalEntryServiceTest(
            JournalEntryService journalEntryService,
            JournalEntryRepository journalEntryRepository,
            FlatRecordRepository flatRecordRepository){
        this.journalEntryService=journalEntryService;
        this.journalEntryRepository=journalEntryRepository;
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

        when(journalEntryRepository.save(any(JournalEntry.class)))
                .thenReturn(this.mockJournalEntry);

        when(journalEntryRecordService.save(any(JournalEntry.class)))
                .thenReturn(GeneralResponseBody.builder()
                        .status(200)
                        .message("{\"Content\":\"List of flat records.... \"}")
                        .timeStamp(System.currentTimeMillis())
                        .build());
    }

    @Test
    @DisplayName("should pass journal entry JSON to DAO to save once")
    public void saveSuccess_UnitTest(){
        GeneralResponseBody result = journalEntryService.save(mockJournalEntry);

        assertEquals(200,result.getStatus());
        verify(journalEntryRepository,times(1)).save(any(JournalEntry.class));
        verify(journalEntryRecordService,times(1)).save(any(JournalEntry.class));
    }

}
