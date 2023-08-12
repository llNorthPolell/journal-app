package com.northpole.journalentryrecordservice.service;

import com.northpole.common.entity.*;
import com.northpole.common.entity.Record;
import com.northpole.journalentryrecordservice.entity.DateAndValue;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordDataSet;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordServiceInput;
import com.northpole.journalentryrecordservice.repository.FlatRecordRepository;
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

    }

    @Test
    @DisplayName("Should take json and flat map to list of FlattenedRecord objects, then call repository save() method")
    public void save_test(){
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

        List<Record> recList1 = new ArrayList<>();
        recList1.add(rec1);
        recList1.add(rec2);
        List<Record> recList2 = new ArrayList<>();
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

        when(flatRecordRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        GeneralResponseBody result = journalEntryRecordService.save(mockJournalEntry);

        assertTrue(result.getMessage().contains("\"count\":5"));
        assertTrue(result.getMessage().contains(
                "\"flatRecords\":[" +
                        "FlatRecord(id=null, dateOfEntry=2022-08-19T00:00, journal=3, topic=My first topic, recKey=a, recValue=1), " +
                        "FlatRecord(id=null, dateOfEntry=2022-08-19T00:00, journal=3, topic=My first topic, recKey=targetA, recValue=2), " +
                        "FlatRecord(id=null, dateOfEntry=2022-08-19T00:00, journal=3, topic=Second, recKey=b, recValue=asdfasdf), " +
                        "FlatRecord(id=null, dateOfEntry=2022-08-19T00:00, journal=3, topic=Second, recKey=c, recValue=5), " +
                        "FlatRecord(id=null, dateOfEntry=2022-08-19T00:00, journal=3, topic=Second, recKey=d, recValue=3)" +
                        "]"
        ));
        verify(flatRecordRepository,times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should take input json and return dataset with dateOfEntry as X and provided recKey field as Y")
    public void getDataset_byDateOfEntry_test(){
        JournalEntryRecordServiceInput testInput = JournalEntryRecordServiceInput.builder()
                .journal(3)
                .topic("My First Post")
                .recKeyX("dateOfEntry")
                .recKeyY("targetA")
                .build();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        DateAndValue x1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19 04:00",formatter),
                "2022-08-19T04:00:00.000Z");


        DateAndValue x2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21 04:00",formatter),
                "2022-08-21T04:00:00.000Z");


        DateAndValue y1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19 04:00",formatter),
                "1");


        DateAndValue y2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21 04:00",formatter),
                "6");

        List<DateAndValue> x = new ArrayList();
        x.add(x1);
        x.add(x2);

        List<DateAndValue> y = new ArrayList<>();
        y.add(y1);
        y.add(y2);

        JournalEntryRecordDataSet mockResult = JournalEntryRecordDataSet.builder()
                .x(x)
                .y(y)
                .build();

        List<JournalEntryRecordDataSet> mockResultList = new ArrayList<>();
        mockResultList.add(mockResult);

        Document mockDoc = new Document();

        AggregationResults<JournalEntryRecordDataSet> mockMongoResult=new AggregationResults<>(mockResultList,mockDoc);

        when(flatRecordRepository.getDataByDateOfEntry(anyInt(),anyString(),anyString())).thenReturn(mockMongoResult);
        JournalEntryRecordDataSet result = journalEntryRecordService.getDataset(testInput);

        assertEquals(mockResult, result);
    }

    @Test
    @DisplayName("Should take input json and return dataset with recKeyX as X and provided recKeyY field as Y")
    public void getDataset_byCustomField_test(){
        JournalEntryRecordServiceInput testInput = JournalEntryRecordServiceInput.builder()
                .journal(3)
                .topic("My First Post")
                .recKeyX("a")
                .recKeyY("targetA")
                .build();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        DateAndValue x1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19 04:00",formatter),
                "2022-08-19T04:00:00.000Z");


        DateAndValue x2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21 04:00",formatter),
                "2022-08-21T04:00:00.000Z");


        DateAndValue y1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19 04:00",formatter),
                "1");


        DateAndValue y2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21 04:00",formatter),
                "6");

        List<DateAndValue> x = new ArrayList();
        x.add(x1);
        x.add(x2);

        List<DateAndValue> y = new ArrayList<>();
        y.add(y1);
        y.add(y2);

        JournalEntryRecordDataSet mockResult = JournalEntryRecordDataSet.builder()
                .x(x)
                .y(y)
                .build();

        List<JournalEntryRecordDataSet> mockResultList = new ArrayList<>();
        mockResultList.add(mockResult);

        Document mockDoc = new Document();

        AggregationResults<JournalEntryRecordDataSet> mockMongoResult=new AggregationResults<>(mockResultList,mockDoc);

        when(flatRecordRepository.getDataByDateOfEntry(anyInt(),anyString(),anyString())).thenReturn(mockMongoResult);
        JournalEntryRecordDataSet result = journalEntryRecordService.getDataset(testInput);

        assertEquals(mockResult, result);
    }
}
