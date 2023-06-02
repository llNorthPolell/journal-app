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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
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
@TestPropertySource(locations="classpath:application-test.properties")
public class JournalEntryPublisherControllerTest {

    private MockMvc mockMvc;

    private JournalEntry mockJournalEntry;

    private ObjectMapper objectMapper;

    private final String ENDPOINT = "/publishJournalEntry";
    private final String MOCK_UUID_STRING="7aa881bc-f6e1-4621-9325-c199d7b3e5c8";

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

        UUID mockUUID = UUID.fromString(MOCK_UUID_STRING);
        when(journalEntryPublisherService.processJournalEntry(any(JournalEntry.class))).thenReturn(mockUUID);
    }

    private enum API_RESULT {
        FAIL(status().isBadRequest()), PASS(status().isOk());

        private ResultMatcher result;

        API_RESULT(ResultMatcher result){
            this.result=result;
        }

        public ResultMatcher value(){
            return this.result;
        }

    }

    private MvcResult postPublishJournalEntry(String testJson,API_RESULT result) throws Exception{
        return mockMvc.perform(
                MockMvcRequestBuilders.post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJson))
                .andExpect(result.value()).andReturn();
    }

    private MvcResult doGenericValidationTest(String keyValueString, String replace, API_RESULT result) throws Exception{
        String testJson = objectMapper.writeValueAsString(mockJournalEntry)
                .replace(keyValueString,replace);
        return postPublishJournalEntry(testJson,result);
    }

    @DisplayName("should pass validation and return UUID")
    @Test
    public void publishJournalEntrySuccess() throws Exception {
        MvcResult mvcResult = doGenericValidationTest("","",API_RESULT.PASS);
        assertTrue(
                mvcResult.getResponse()
                        .getContentAsString()
                        .startsWith("{\"status\":200,\"message\":\"" + MOCK_UUID_STRING + "\"")
        );
    }

    @DisplayName("should fail validation when journal is missing")
    @Test
    public void publishJournalEntryMissingJournal() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"journal\":3,",
                "",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail validation when summary is missing")
    @Test
    public void publishJournalEntryMissingSummary() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"summary\":\"My First Post\",",
                "",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail validation when summary is empty")
    @Test
    public void publishJournalEntryEmptySummary() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"summary\":\"My First Post\",",
                "\"summary\":\"\",",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail when overview is missing")
    @Test
    public void publishJournalEntryMissingOverview() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"overview\":\"Woohoo! My First Post!! test\",",
                "",
                API_RESULT.FAIL);
    }

    @DisplayName("should pass when overview is empty")
    @Test
    public void publishJournalEntryEmptyOverview() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"overview\":\"Woohoo! My First Post!! test\",",
                "\"overview\":\"\",",
                API_RESULT.PASS);

        assertTrue(
                mvcResult.getResponse()
                        .getContentAsString()
                        .startsWith("{\"status\":200,\"message\":\"" + MOCK_UUID_STRING + "\"")
        );
    }

    @DisplayName("should fail when dateOfEntry is missing")
    @Test
    public void publishJournalEntryMissingDateOfEntry() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"dateOfEntry\":\"2022-08-19T00:00:00\",",
                "",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail when journal entry body is missing")
    @Test
    public void publishJournalEntryMissingJournalBody() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                ",\"journalBodyItems\":[{\"topic\":\"My first topic\",\"description\":\"My first topic ever!!!\",\"recordList\":[{\"recKey\":\"a\",\"recValue\":\"1\"},{\"recKey\":\"targetA\",\"recValue\":\"2\"}]",
                "",
                API_RESULT.FAIL);
    }


    @DisplayName("should fail when a topic in the journal entry body is missing")
    @Test
    public void publishJournalEntryMissingTopic() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"topic\":\"My first topic\",",
                "",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail when a topic in the journal entry body is empty")
    @Test
    public void publishJournalEntryEmptyTopic() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"topic\":\"My first topic\",",
                "\"topic\":\"\",",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail when a description in the journal entry body is missing")
    @Test
    public void publishJournalEntryMissingDescription() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"description\":\"This is my second topic...\",",
                "",
                API_RESULT.FAIL);
    }

    @DisplayName("should pass when a description in the journal entry body is empty")
    @Test
    public void publishJournalEntryEmptyDescription() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"description\":\"This is my second topic...\",",
                "\"description\":\"\",",
                API_RESULT.PASS);

        assertTrue(
                mvcResult.getResponse()
                        .getContentAsString()
                        .startsWith("{\"status\":200,\"message\":\"" + MOCK_UUID_STRING + "\"")
        );
    }

    @DisplayName("should fail when a record list in the journal entry body is missing")
    @Test
    public void publishJournalEntryMissingRecordList() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                ",\"recordList\":[{\"recKey\":\"a\",\"recValue\":\"1\"},{\"recKey\":\"targetA\",\"recValue\":\"2\"}]",
                "",
                API_RESULT.FAIL);
    }


    @DisplayName("should fail when a recKey in the record list is missing")
    @Test
    public void publishJournalEntryMissingRecKey() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"recKey\":\"a\",",
                "",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail when a recKey in the record list is empty")
    @Test
    public void publishJournalEntryEmptyRecKey() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                "\"recKey\":\"a\"",
                "\"recKey\":\"\"",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail when a recValue in the record list is missing")
    @Test
    public void publishJournalEntryMissingRecValue() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                ",\"recValue\":\"2\"",
                "",
                API_RESULT.FAIL);
    }

    @DisplayName("should fail when a recValue in the record list is empty")
    @Test
    public void publishJournalEntryEmptyRecValue() throws Exception {
        MvcResult mvcResult = doGenericValidationTest(
                ",\"recValue\":\"2\"",
                ",\"recValue\":\"\"",
                API_RESULT.FAIL);
    }
}
