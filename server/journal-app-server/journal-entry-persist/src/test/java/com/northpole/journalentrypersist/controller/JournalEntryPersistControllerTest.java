package com.northpole.journalentrypersist.controller;


import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalBodyItem;
import com.northpole.common.entity.JournalEntry;
import com.northpole.common.entity.Record;
import com.northpole.journalentrypersist.repository.JournalEntryRepository;
import com.northpole.journalentrypersist.service.JournalEntryPersistService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JournalEntryPersistControllerTest {

    @Mock
    private JournalEntryPersistService journalEntryPersistService;

    @InjectMocks
    private JournalEntryPersistController journalEntryPersistController;

    private JournalEntry mockJournalEntry;

    private JournalEntryRepository journalEntryRepository;


    @Autowired
    public JournalEntryPersistControllerTest(
            JournalEntryPersistController journalEntryPersistController,
            JournalEntryPersistService journalEntryPersistService,
            JournalEntryRepository journalEntryRepository){
        this.journalEntryPersistController=journalEntryPersistController;
        this.journalEntryPersistService=journalEntryPersistService;
        this.journalEntryRepository=journalEntryRepository;
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
                .entryId(UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7b3e5c8"))
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
                .journalBodyItems(bodyList)
                .build();

    }

    @Test
    @DisplayName("Should pass and run journalEntryPersistService.save() method once")
    public void saveSuccess_UnitTest(){
        when(journalEntryPersistService.save(any(JournalEntry.class)))
                .thenReturn(GeneralResponseBody.builder().status(200).build());

        GeneralResponseBody result = journalEntryPersistController.listen(mockJournalEntry);
        assertNotNull(result);
        assertEquals(200,result.getStatus());
        verify(journalEntryPersistService,times(1)).save(any(JournalEntry.class));
    }

}
