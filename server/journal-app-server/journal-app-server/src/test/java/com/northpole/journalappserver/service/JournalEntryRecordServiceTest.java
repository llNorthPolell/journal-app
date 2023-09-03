package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.*;
import com.northpole.journalappserver.entity.Record;
import com.northpole.journalappserver.repository.FlatRecordRepository;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JournalEntryRecordServiceTest {
    @Mock
    private FlatRecordRepository flatRecordRepository;

    @InjectMocks
    private JournalEntryRecordService journalEntryRecordService;

    private JournalEntry mockJournalEntry;

    private List<FlatRecord> mockFlatRecords;

    @Autowired
    public JournalEntryRecordServiceTest(
            JournalEntryRecordService journalEntryRecordService,
            FlatRecordRepository flatRecordRepository
    ){
        this.journalEntryRecordService = journalEntryRecordService;
        this.flatRecordRepository= flatRecordRepository;
    }

    @BeforeEach
    public void setupEachTest (){
        List<Record> recList1 = new ArrayList<>();
        List<Record> recList2 = new ArrayList<>();
        mockFlatRecords= new ArrayList<>();

        Record rec1 = Record.builder()
                .recKey("a")
                .recValue("1")
                .build();

        Record rec2 = Record.builder()
                .recKey("targetA")
                .recValue("2")
                .build();

        Record rec3 = Record.builder()
                .recKey("b")
                .recValue("asdfasdf")
                .build();

        Record rec4 = Record.builder()
                .recKey("c")
                .recValue("5")
                .build();

        Record rec5 = Record.builder()
                .recKey("d")
                .recValue("3")
                .build();


        recList1.add(rec1);
        recList1.add(rec2);
        recList2.add(rec3);
        recList2.add(rec4);
        recList2.add(rec5);

        JournalBodyItem body1 = JournalBodyItem.builder()
                .topic("My first topic")
                .description("My first topic ever!!!")
                .recordList(recList1)
                .build();
        JournalBodyItem body2 = JournalBodyItem.builder()
                .topic("Second")
                .description("This is my second topic...")
                .recordList(recList2)
                .build();
        JournalBodyItem body3 = JournalBodyItem.builder()
                .topic("Second")
                .description("This is my third topic.")
                .recordList(new ArrayList<>())
                .build();

        List<JournalBodyItem> bodyList = new ArrayList<>();
        bodyList.add(body1);
        bodyList.add(body2);
        bodyList.add(body3);

        this.mockJournalEntry = JournalEntry.builder()
                .journal(3)
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
                .journalBodyItems(bodyList)
                .build();


        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("a5e6346d-9967-4e23-b6b5-baaa9e619a1c"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(3)
                        .recKey(rec1.getRecKey())
                        .recValue(rec1.getRecValue())
                        .topic(body1.getTopic())
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("3c13ecdd-f125-425e-a535-51bf093001cc"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(3)
                        .recKey(rec2.getRecKey())
                        .recValue(rec2.getRecValue())
                        .topic(body1.getTopic())
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("2fcef89c-2f5c-461c-8411-539dd949605e"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(3)
                        .recKey(rec3.getRecKey())
                        .recValue(rec3.getRecValue())
                        .topic(body2.getTopic())
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("8bd95d32-9a34-42f9-93c3-95bb7d431549"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(3)
                        .recKey(rec4.getRecKey())
                        .recValue(rec4.getRecValue())
                        .topic(body2.getTopic())
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("271aa723-18be-4049-a9d9-61c18da98d92"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(3)
                        .recKey(rec5.getRecKey())
                        .recValue(rec5.getRecValue())
                        .topic(body1.getTopic())
                        .build()
        );

        when(flatRecordRepository.saveAll(anyList())).thenReturn(mockFlatRecords);
    }

    @Test
    @DisplayName("Should take json and flat map to list of FlattenedRecord objects, then call repository save() method")
    public void save_UnitTest(){
        GeneralResponseBody result = journalEntryRecordService.save(mockJournalEntry);

        assertTrue(result.getMessage().contains("\"count\":5"));
        verify(flatRecordRepository,times(1)).saveAll(anyList());
    }

}
