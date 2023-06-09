package com.northpole.journalentrypersist.repository;

import com.northpole.common.entity.JournalBodyItem;
import com.northpole.common.entity.JournalEntry;
import com.northpole.common.entity.Record;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JournalEntryRepositoryTest {

    private JournalEntryRepository journalEntryRepository;

    private JournalEntry mockJournalEntry;

    @Autowired
    public JournalEntryRepositoryTest(JournalEntryRepository journalEntryRepository){
        this.journalEntryRepository = journalEntryRepository;
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

    }

    @AfterEach
    public void cleanUpAfterEachTest(){
        journalEntryRepository.delete(mockJournalEntry);
    }



    @Test
    @DisplayName("should save to data source")
    public void saveSuccess_IntegrationTest(){
        journalEntryRepository.save(mockJournalEntry);
        Optional<JournalEntry> searchResult = journalEntryRepository.findById(mockJournalEntry.getEntryId());
        assertNotEquals(Optional.empty(),searchResult);
    }

}
