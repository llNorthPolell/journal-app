package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.service.JournalEntryRecordService;
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


}
