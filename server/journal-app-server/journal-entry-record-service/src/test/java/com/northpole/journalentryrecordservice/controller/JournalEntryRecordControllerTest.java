package com.northpole.journalentryrecordservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.common.entity.FlatRecord;
import com.northpole.journalentryrecordservice.entity.DateAndValue;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordDataSet;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordServiceInput;
import com.northpole.journalentryrecordservice.service.JournalEntryRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class JournalEntryRecordControllerTest {
    @MockBean
    private JournalEntryRecordService journalEntryRecordService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private JournalEntryRecordController journalEntryRecordController;

    private MockMvc mockMvc;

    private final String GET_ALL_RECORDS_ENDPOINT ="/records";

    private final String GET_DATA_BY_DATE_ENDPOINT = "/records/dataset";

    private List<FlatRecord> mockGetRecordsResult = new ArrayList<>();
    private JournalEntryRecordDataSet mockGetDatasetByDateResult = null;
    private JournalEntryRecordDataSet mockGetDatasetByCustomFieldResult = null;

    @Autowired
    public JournalEntryRecordControllerTest(
            JournalEntryRecordService journalEntryRecordService,
            JournalEntryRecordController journalEntryRecordController,
            ObjectMapper objectMapper,
            MockMvc mockMvc
    ){
        this.journalEntryRecordService=journalEntryRecordService;
        this.journalEntryRecordController=journalEntryRecordController;
        this.objectMapper= objectMapper;
        this.mockMvc=mockMvc;
    }


    private void setupMockGetRecords(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        mockGetRecordsResult.add(
                FlatRecord.builder()
                        .id(UUID.fromString("47d7c9df-af49-4602-ab11-cd224ee2b0fa"))
                        .journal(3)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("3")
                        .dateOfEntry(LocalDateTime.parse("2022-08-20T04:00:00",formatter))
                        .build()
        );

        mockGetRecordsResult.add(
                FlatRecord.builder()
                        .id(UUID.fromString("32c5a79e-ef77-41b2-9fe2-a683e93e2f4a"))
                        .journal(3)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00",formatter))
                        .build()
        );

        mockGetRecordsResult.add(
                FlatRecord.builder()
                        .id(UUID.fromString("d2f393ec-1cae-4954-9b55-f20d408657be"))
                        .journal(3)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00",formatter))
                        .build()
        );

        JournalEntryRecordServiceInput mockGetRecordsInput = JournalEntryRecordServiceInput.builder()
                .journal(3)
                .topic("Second")
                .recKey("a")
                .build();

        when(journalEntryRecordService.getRecords(mockGetRecordsInput)).thenReturn(mockGetRecordsResult);
    }

    private void setupMockGetDataByDateOfEntry(){
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        DateAndValue x1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19T04:00",formatter2),
                "2022-08-19T04:00:00.000Z");


        DateAndValue x2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21T04:00",formatter2),
                "2022-08-21T04:00:00.000Z");


        DateAndValue y1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19T04:00",formatter2),
                "1");


        DateAndValue y2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21T04:00",formatter2),
                "6");

        List<DateAndValue> x = new ArrayList();
        x.add(x1);
        x.add(x2);

        List<DateAndValue> y = new ArrayList<>();
        y.add(y1);
        y.add(y2);

        mockGetDatasetByDateResult = JournalEntryRecordDataSet.builder()
                .x(x)
                .y(y)
                .build();

        JournalEntryRecordServiceInput mockGetByDateOfEntryInput = JournalEntryRecordServiceInput.builder()
                .journal(3)
                .topic("Second")
                .recKeyX("dateOfEntry")
                .recKeyY("a")
                .build();
        when(journalEntryRecordService.getDataset(mockGetByDateOfEntryInput)).thenReturn(mockGetDatasetByDateResult);
    }

    private void setupGetDataByCustomField(){
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        JournalEntryRecordServiceInput mockGetByCustomFieldInput = JournalEntryRecordServiceInput.builder()
                .journal(3)
                .topic("Second")
                .recKeyX("a")
                .recKeyY("targetA")
                .build();

        DateAndValue x1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19T04:00",formatter2),
                "1");


        DateAndValue x2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21T04:00",formatter2),
                "6");


        DateAndValue y1 = new DateAndValue(
                LocalDateTime.parse("2022-08-19T04:00",formatter2),
                "2");


        DateAndValue y2 = new DateAndValue(
                LocalDateTime.parse("2022-08-21T04:00",formatter2),
                "3");

        List<DateAndValue> x = new ArrayList();
        x.add(x1);
        x.add(x2);

        List<DateAndValue> y = new ArrayList<>();
        y.add(y1);
        y.add(y2);

        mockGetDatasetByCustomFieldResult = JournalEntryRecordDataSet.builder()
                .x(x)
                .y(y)
                .build();


        when(journalEntryRecordService.getDataset(mockGetByCustomFieldInput)).thenReturn(mockGetDatasetByCustomFieldResult);
    }

    @BeforeEach
    public void setup(){
        setupMockGetRecords();

        setupMockGetDataByDateOfEntry();

        setupGetDataByCustomField();
    }

    @Test
    @DisplayName("Should get all records with input criteria successfully")
    public void getRecords_UnitTest() throws Exception {
        String testJson = "{\"journal\":3,\"topic\":\"Second\",\"recKey\":\"a\"}";

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(GET_ALL_RECORDS_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testJson)
                )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(
                objectMapper.writeValueAsString(mockGetRecordsResult),
                mvcResult.getResponse().getContentAsString()
        );
    }


    @Test
    @DisplayName("Should get JSON with x as array of dateOfEntry and y as array of custom record values")
    public void getDataByDateOfEntry_UnitTest() throws Exception {
        String testJson = "{\"journal\":3,\"topic\":\"Second\",\"recKeyX\":\"dateOfEntry\",\"recKeyY\":\"a\"}";

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(GET_DATA_BY_DATE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJson)
        )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(
                objectMapper.writeValueAsString(mockGetDatasetByDateResult),
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    @DisplayName("Should get JSON with x as array of custom record and y as array of custom record values")
    public void getDataByCustomField_UnitTest() throws Exception {
        String testJson = "{\"journal\":3,\"topic\":\"Second\",\"recKeyX\":\"a\",\"recKeyY\":\"targetA\"}";

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post(GET_DATA_BY_DATE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testJson)
                )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(
                objectMapper.writeValueAsString(mockGetDatasetByCustomFieldResult),
                mvcResult.getResponse().getContentAsString()
        );
    }
}
