package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalBodyItem;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.entity.Record;
import com.northpole.journalappserver.service.JournalEntryService;
import com.northpole.journalappserver.util.enums.APIResult;
import com.northpole.journalappserver.util.enums.GenericAPITestUtil;
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
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
public class JournalEntryControllerTest {

    private MockMvc mockMvc;

    private JournalEntry mockJournalEntry;

    private ObjectMapper objectMapper;

    private final String ENDPOINT = "/journalEntry";
    private final String MOCK_UUID_STRING="7aa881bc-f6e1-4621-9325-c199d7b3e5c8";

    private long testTimestamp = System.currentTimeMillis();
    private GeneralResponseBody mockSuccessResult=null;

    private GenericAPITestUtil genericAPITestUtil;

    @MockBean
    private JournalEntryService journalEntryService;

    @Autowired
    public JournalEntryControllerTest(MockMvc mockMvc,ObjectMapper objectMapper){
        this.mockMvc=mockMvc;
        this.objectMapper=objectMapper;
        this.genericAPITestUtil = new GenericAPITestUtil(
                this.mockMvc,
                this.objectMapper,
                this.ENDPOINT
        );
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
        this.mockSuccessResult = GeneralResponseBody.builder()
                .status(200)
                .message("{\"id\":\""+mockUUID+"\"}")
                .timeStamp(testTimestamp)
                .build();

        when(journalEntryService.save(any(JournalEntry.class)))
                .thenReturn(mockSuccessResult);
    }

    @DisplayName("Should pass validation and return UUID")
    @Test
    public void publishJournalEntrySuccess_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "",
                "",
                APIResult.PASS);
        assertEquals(
                objectMapper.writeValueAsString(mockSuccessResult),
                mvcResult.getResponse().getContentAsString()
        );
    }

    @DisplayName("Should fail validation when journal is missing")
    @Test
    public void publishJournalEntryMissingJournal_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"journal\":3,",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should fail validation when summary is missing")
    @Test
    public void publishJournalEntryMissingSummary_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"summary\":\"My First Post\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should fail validation when summary is empty")
    @Test
    public void publishJournalEntryEmptySummary_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"summary\":\"My First Post\",",
                "\"summary\":\"\",",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when overview is missing")
    @Test
    public void publishJournalEntryMissingOverview_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"overview\":\"Woohoo! My First Post!! test\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should pass when overview is empty")
    @Test
    public void publishJournalEntryEmptyOverview_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"overview\":\"Woohoo! My First Post!! test\",",
                "\"overview\":\"\",",
                APIResult.PASS);

        assertEquals(
                objectMapper.writeValueAsString(mockSuccessResult),
                mvcResult.getResponse().getContentAsString()
        );
    }

    @DisplayName("Should fail when dateOfEntry is missing")
    @Test
    public void publishJournalEntryMissingDateOfEntry_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"dateOfEntry\":\"2022-08-19T00:00:00\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when journal entry body is missing")
    @Test
    public void publishJournalEntryMissingJournalBody_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                ",\"journalBodyItems\":[{\"topic\":\"My first topic\",\"description\":\"My first topic ever!!!\",\"recordList\":[{\"recKey\":\"a\",\"recValue\":\"1\"},{\"recKey\":\"targetA\",\"recValue\":\"2\"}]",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should pass when journal entry body is empty")
    @Test
    public void publishJournalEntryEmptyJournalBody_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                ",\"journalBodyItems\":[{\"topic\":\"My first topic\",\"description\":\"My first topic ever!!!\",\"recordList\":[{\"recKey\":\"a\",\"recValue\":\"1\"},{\"recKey\":\"targetA\",\"recValue\":\"2\"}]",
                "\"journalBodyItems\":[]",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when a topic in the journal entry body is missing")
    @Test
    public void publishJournalEntryMissingTopic_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"topic\":\"My first topic\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when a topic in the journal entry body is empty")
    @Test
    public void publishJournalEntryEmptyTopic_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"topic\":\"My first topic\",",
                "\"topic\":\"\",",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when a description in the journal entry body is missing")
    @Test
    public void publishJournalEntryMissingDescription_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"description\":\"This is my second topic...\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should pass when a description in the journal entry body is empty")
    @Test
    public void publishJournalEntryEmptyDescription_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"description\":\"This is my second topic...\",",
                "\"description\":\"\",",
                APIResult.PASS);

        assertEquals(
                objectMapper.writeValueAsString(mockSuccessResult),
                mvcResult.getResponse().getContentAsString()
        );
    }

    @DisplayName("Should fail when a record list in the journal entry body is missing")
    @Test
    public void publishJournalEntryMissingRecordList_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                ",\"recordList\":[{\"recKey\":\"a\",\"recValue\":\"1\"},{\"recKey\":\"targetA\",\"recValue\":\"2\"}]",
                "",
                APIResult.FAIL);
    }


    @DisplayName("Should fail when a recKey in the record list is missing")
    @Test
    public void publishJournalEntryMissingRecKey_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"recKey\":\"a\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when a recKey in the record list is empty")
    @Test
    public void publishJournalEntryEmptyRecKey_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                "\"recKey\":\"a\"",
                "\"recKey\":\"\"",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when a recValue in the record list is missing")
    @Test
    public void publishJournalEntryMissingRecValue_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                ",\"recValue\":\"2\"",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when a recValue in the record list is empty")
    @Test
    public void publishJournalEntryEmptyRecValue_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockJournalEntry,
                ",\"recValue\":\"2\"",
                ",\"recValue\":\"\"",
                APIResult.FAIL);
    }
}
