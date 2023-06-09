package com.northpole.journalentrypersist.service;

import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalBodyItem;
import com.northpole.common.entity.JournalEntry;
import com.northpole.common.entity.Record;
import com.northpole.journalentrypersist.repository.JournalEntryRepository;
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
public class JournalEntryPersistServiceTest {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @InjectMocks
    private JournalEntryPersistService journalEntryPersistService;

    private JournalEntry mockJournalEntry;

    @Autowired
    public JournalEntryPersistServiceTest(
            JournalEntryPersistService journalEntryPersistService,
            JournalEntryRepository journalEntryRepository){
        this.journalEntryPersistService=journalEntryPersistService;
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
    }

    @Test
    @DisplayName("should pass journal entry JSON to DAO to save once")
    public void saveSuccess_UnitTest(){
        GeneralResponseBody result = journalEntryPersistService.save(mockJournalEntry);

        assertEquals(200,result.getStatus());
        verify(journalEntryRepository,times(1)).save(any(JournalEntry.class));
    }

}
