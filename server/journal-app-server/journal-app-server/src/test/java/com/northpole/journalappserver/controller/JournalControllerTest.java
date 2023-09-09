package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.Journal;
import com.northpole.journalappserver.service.JournalService;
import com.northpole.journalappserver.util.enums.APIResult;
import com.northpole.journalappserver.util.enums.GenericAPITestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class JournalControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final UUID MOCK_JOURNAL_REF = UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");
    private final String ENDPOINT = "/journals";

    private GenericAPITestUtil genericAPITestUtil;

    private Journal mockPayload;

    private String expectedSaveSuccessResult;

    @MockBean
    private JournalService journalService;

    @Autowired
    public JournalControllerTest(MockMvc mockMvc,ObjectMapper objectMapper){
        this.mockMvc=mockMvc;
        this.objectMapper=objectMapper;
        this.genericAPITestUtil = new GenericAPITestUtil(
                this.mockMvc,
                this.objectMapper,
                this.ENDPOINT
        );
    }

    @BeforeEach
    public void setupBeforeEachTest(){
        mockPayload = Journal.builder()
                .name("test")
                .img("some/location.png")
                .build();

        expectedSaveSuccessResult = "{\"id\":\""+ MOCK_JOURNAL_REF + "\"}";

        when(journalService.createJournal(anyString(),any(Journal.class)))
                .thenReturn(Journal.builder()
                        .journalId(1)
                        .journalRef(MOCK_JOURNAL_REF)
                        .build()
                );
    }

    @Test
    @DisplayName("Should pass validation and return UUID")
    public void saveSuccess_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "",
                "",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    @DisplayName("Should fail validation when name is missing")
    public void saveJournalMissingName_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"name\":\"test\"",
                "",
                APIResult.FAIL);
    }

    @Test
    @DisplayName("Should pass validation when image is missing and return UUID")
    public void saveJournalMissingImage_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                ",\"img\":\"some/location.png\"",
                "",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }
}
