package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.WidgetDataConfig;
import com.northpole.journalappserver.service.DashboardWidgetService;
import com.northpole.journalappserver.util.enums.APIResult;
import com.northpole.journalappserver.util.enums.GenericAPITestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class DashboardWidgetControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private DashboardWidget mockPayload;

    private GeneralResponseBody mockSuccessResult;

    private final String ENDPOINT = "/dashboard";

    private final int MOCK_ID=3;

    private final long MOCK_TIMESTAMP = System.currentTimeMillis();

    private GenericAPITestUtil genericAPITestUtil;

    @MockBean
    private DashboardWidgetService dashboardWidgetService;

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
        HttpStatus status = HttpStatus.OK;
        mockSuccessResult = GeneralResponseBody.builder()
                        .status(status.value())
                        .message("{\"id\":\""+MOCK_ID+"\"}")
                        .timeStamp(MOCK_TIMESTAMP)
                        .build();

        when(dashboardWidgetService.createDashboardWidget(any(DashboardWidget.class)))
                .thenReturn(mockSuccessResult);

        List<WidgetDataConfig> mockConfigs = new ArrayList<>();
        mockConfigs.add(
                WidgetDataConfig.builder()
                        .type("x")
                        .label("Test X")
                        .rule("a")
                        .build()
        );

        mockConfigs.add(
                WidgetDataConfig.builder()
                        .type("y")
                        .label("Test Y")
                        .rule("b")
                        .build()
        );

        mockPayload = DashboardWidget.builder()
                .journal(3)
                .title("Test Widget")
                .type("line-graph")
                .configs(mockConfigs)
                .build();
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
                objectMapper.writeValueAsString(mockSuccessResult),
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    @DisplayName("Should fail when journal is missing")
    public void publicDashboardWidgetMissingJournal_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                "\"journal\":3,",
                "",
                APIResult.FAIL);
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
                "\"title\":\"Test Widget\",",
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
                ":\"a\"",
                ":\"a\",\"color\":\"#ZABCD\"",
                APIResult.FAIL);
    }

    @Test
    @DisplayName("Should save successfully if config color is in hex format")
    public void publishDashboardWidgetSuccessWithConfigColor_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                ":\"a\"",
                ":\"a\",\"color\":\"#FFFFFF\"",
                APIResult.PASS);
        assertEquals(
                objectMapper.writeValueAsString(mockSuccessResult),
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    @DisplayName("Should save successfully if config color is in hex format (lower case)")
    public void publishDashboardWidgetSuccessWithConfigColorLowercase_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockPayload,
                ":\"a\"",
                ":\"a\",\"color\":\"#ffffff\"",
                APIResult.PASS);
        assertEquals(
                objectMapper.writeValueAsString(mockSuccessResult),
                mvcResult.getResponse().getContentAsString()
        );
    }

}
