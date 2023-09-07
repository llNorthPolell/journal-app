package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.WidgetDataConfig;
import com.northpole.journalappserver.service.DashboardWidgetService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class DashboardWidgetControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private DashboardWidget mockPayload;


    private String expectedSaveSuccessResult;

    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");
    private final String ENDPOINT = "/"+MOCK_JOURNAL_REF+"/dashboard";

    private final int MOCK_ID=3;

    private GenericAPITestUtil genericAPITestUtil;

    @MockBean
    private DashboardWidgetService dashboardWidgetService;

    @MockBean
    private JournalService journalService;

    @Autowired
    public DashboardWidgetControllerTest(MockMvc mockMvc, ObjectMapper objectMapper){
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
        List<WidgetDataConfig> configs = new ArrayList<>();

        mockPayload = DashboardWidget.builder()
                .type("line-graph")
                .title("Widget Creation Test")
                .configs(configs)
                .build();


        configs.add(
                WidgetDataConfig.builder()
                        .type("x")
                        .label("Test X")
                        .rule("a")
                        .build()
        );
        configs.add(
                WidgetDataConfig.builder()
                        .type("y")
                        .label("Test Y")
                        .rule("b")
                        .color("#a1b298")
                        .build()
        );

        DashboardWidget mockSaveResult = DashboardWidget.builder()
                .id(1)
                .journal(3)
                .type("line-graph")
                .build();


        when(dashboardWidgetService.createDashboardWidget(any(UUID.class),any(DashboardWidget.class)))
                .thenReturn(mockSaveResult);

        when(journalService.ownsJournal(anyString(), any(UUID.class)))
                .thenReturn(true);

        expectedSaveSuccessResult = "{\"id\":1}";

    }



    @Test
    @DisplayName("Should save successfully")
    public void publishDashboardWidgetSuccess_UnitTest() throws Exception {
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
    @DisplayName("Should fail when widget type is missing")
    public void publicDashboardWidgetMissingType_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"type\":\"line-graph\",",
                "",
                APIResult.FAIL);
    }

    @Test
    @DisplayName("Should fail when title is missing")
    public void publicDashboardWidgetMissingTitle_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"title\":\"Widget Creation Test\",",
                "",
                APIResult.FAIL);
    }

    @Test
    @DisplayName("Should fail when type field is missing in a config")
    public void publicDashboardWidgetMissingConfigType_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"type\":\"x\",",
                "",
                APIResult.FAIL);
    }

    @Test
    @DisplayName("Should fail when label field is missing in a config")
    public void publicDashboardWidgetMissingConfigLabel_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"label\":\"Test Y\",",
                "",
                APIResult.FAIL);
    }

    @Test
    @DisplayName("Should fail when rule field is missing in a config")
    public void publicDashboardWidgetMissingConfigRule_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"rule\":\"a\"",
                "",
                APIResult.FAIL);
    }

    @Test
    @DisplayName("Should fail when color field is not hex format in a config")
    public void publicDashboardWidgetInvalidConfigColor_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"rule\":\"a\"",
                "\"rule\":\"a\",\"color\":\"#ZABCD\"",
                APIResult.FAIL);
    }

    @Test
    @DisplayName("Should save successfully if config color is in hex format")
    public void publishDashboardWidgetSuccessWithConfigColor_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"rule\":\"a\"",
                "\"rule\":\"a\",\"color\":\"#FFFFFF\"",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    @DisplayName("Should save successfully if config color is in hex format (lower case)")
    public void publishDashboardWidgetSuccessWithConfigColorLowercase_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"rule\":\"a\"",
                "\"rule\":\"a\",\"color\":\"#ffffff\"",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }

}
