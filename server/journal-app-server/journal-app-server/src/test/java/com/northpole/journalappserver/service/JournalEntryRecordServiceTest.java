package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.util.ReflectionTestUtils;

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

    @Mock
    private JournalService journalService;

    @InjectMocks
    private JournalEntryRecordService journalEntryRecordService;

    private JournalEntry mockJournalEntry;

    private List<FlatRecord> mockFlatRecords;

    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");
    private final UUID MOCK_JOURNAL_ENTRY_ID=UUID.fromString("08cfe10d-4ddb-4837-b1d4-a11e01ad36c6");

    @Autowired
    public JournalEntryRecordServiceTest(
            JournalEntryRecordService journalEntryRecordService,
            JournalService journalService,
            FlatRecordRepository flatRecordRepository
    ){
        this.journalEntryRecordService = journalEntryRecordService;
        this.journalService=journalService;
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
                .journal(UUID.fromString("b38e05e1-b4f7-4696-a87d-d830a25eb65d"))
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
                .journalBodyItems(bodyList)
                .build();


        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("a5e6346d-9967-4e23-b6b5-baaa9e619a1c"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(MOCK_JOURNAL_REF)
                        .recKey(rec1.getRecKey())
                        .recValue(rec1.getRecValue())
                        .topic(body1.getTopic())
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("3c13ecdd-f125-425e-a535-51bf093001cc"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(MOCK_JOURNAL_REF)
                        .recKey(rec2.getRecKey())
                        .recValue(rec2.getRecValue())
                        .topic(body1.getTopic())
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("2fcef89c-2f5c-461c-8411-539dd949605e"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(MOCK_JOURNAL_REF)
                        .recKey(rec3.getRecKey())
                        .recValue(rec3.getRecValue())
                        .topic(body2.getTopic())
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("8bd95d32-9a34-42f9-93c3-95bb7d431549"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(MOCK_JOURNAL_REF)
                        .recKey(rec4.getRecKey())
                        .recValue(rec4.getRecValue())
                        .topic(body2.getTopic())
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.fromString("271aa723-18be-4049-a9d9-61c18da98d92"))
                        .dateOfEntry(mockJournalEntry.getDateOfEntry())
                        .journal(MOCK_JOURNAL_REF)
                        .recKey(rec5.getRecKey())
                        .recValue(rec5.getRecValue())
                        .topic(body1.getTopic())
                        .build()
        );

        List<FlatRecord> mockFindAllByJournalEntryResult = new ArrayList<>();

        mockFindAllByJournalEntryResult.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .build()
        );



        when(flatRecordRepository.saveAll(anyList())).thenReturn(mockFlatRecords);
        when(flatRecordRepository.findAllByJournalEntry(any(UUID.class))).thenReturn(mockFindAllByJournalEntryResult);
        when(journalService.getJournalId(any(UUID.class))).thenReturn(3);

        ReflectionTestUtils.setField(journalEntryRecordService, "flatRecordRepository", flatRecordRepository);
    }

    @Test
    @DisplayName("Should take json and flat map to list of FlattenedRecord objects, then call repository save() method")
    public void save_UnitTest() {

        List<FlatRecord> results = journalEntryRecordService.save(mockJournalEntry);

        assertEquals(5, results.size());
        verify(flatRecordRepository, times(1)).saveAll(anyList());

    }

    @Test
    @DisplayName("Should call flatRecordRepository findAllByJournalEntry once, saveAll twice and " +
            "deleteAll once to update")
    public void updateRelatedFlatRecords_UnitTest(){
        List<FlatRecord> results = journalEntryRecordService.updateRelatedFlatRecords(
                MOCK_JOURNAL_ENTRY_ID, mockJournalEntry);

        verify(flatRecordRepository, times(1)).findAllByJournalEntry(any(UUID.class));
        verify(flatRecordRepository,times(1)).deleteAll(any(List.class));
        verify(flatRecordRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should call flatRecordRepository findAllByJournalEntry once and " +
            "deleteAll once to delete")
    public void deleteRelatedFlatRecords_UnitTest(){
        List<FlatRecord> results = journalEntryRecordService.deleteRelatedFlatRecords(
                MOCK_JOURNAL_ENTRY_ID);

        verify(flatRecordRepository, times(1)).findAllByJournalEntry(any(UUID.class));
        verify(flatRecordRepository, times(1)).deleteAll(anyList());
    }
}
