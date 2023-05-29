package com.northpole.journalentrypublisher.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalentrypublisher.entity.JournalBodyItem;
import com.northpole.journalentrypublisher.entity.JournalEntry;
import com.northpole.journalentrypublisher.entity.Record;
import com.northpole.journalentrypublisher.service.JournalEntryPublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
public class JournalEntryPublisherControllerTest {

    private MockMvc mockMvc;

    private JournalEntry mockJournalEntry;

    private ObjectMapper objectMapper;

    @MockBean
    private JournalEntryPublisherService journalEntryPublisherService;

    @Autowired
    public JournalEntryPublisherControllerTest(MockMvc mockMvc,ObjectMapper objectMapper){
        this.mockMvc=mockMvc;
        this.objectMapper=objectMapper;
    }

    @BeforeEach
    public void setupEachTest(){
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
    }

    @DisplayName("should pass validation and return UUID")
    @Test
    public void publishJournalEntrySuccess() throws Exception {
        UUID mockUUID = UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7b3e5c8");
        when(journalEntryPublisherService.processJournalEntry(any(JournalEntry.class))).thenReturn(mockUUID);
        String testJson = objectMapper.writeValueAsString(mockJournalEntry);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/publishJournalEntry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJson))
                .andExpect(status().isOk()).andReturn();

        assertTrue(
                mvcResult.getResponse()
                        .getContentAsString()
                        .startsWith("{\"status\":200,\"message\":\"" + mockUUID + "\"")
        );
    }

    @DisplayName("should fail validation when journal is missing")
    @Test
    public void publishJournalEntryMissingJournal() throws Exception {
        mockJournalEntry.setJournal(null);
        UUID mockUUID = UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7b3e5c8");
        when(journalEntryPublisherService.processJournalEntry(any(JournalEntry.class))).thenReturn(mockUUID);
        String testJson = objectMapper.writeValueAsString(mockJournalEntry);
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/publishJournalEntry")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testJson))
                .andExpect(status().is4xxClientError()).andReturn();
    }
}
