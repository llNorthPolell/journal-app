package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.*;
import com.northpole.journalappserver.service.GoalTrackerService;
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

@SpringBootTest
@AutoConfigureMockMvc
public class GoalControllerTest {

    private MockMvc mockMvc;

    private Goal mockGoal;

    private ObjectMapper objectMapper;


    private final String MOCK_SAVE_UUID_STRING="82605ca2-0b83-47ca-b0b4-0836991a2df0";
    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");
    private final String ENDPOINT = "/"+MOCK_JOURNAL_REF+"/goals";

    private String expectedSaveSuccessResult;

    private GenericAPITestUtil genericAPITestUtil;

    @MockBean
    private GoalTrackerService goalTrackerService;

    @MockBean
    private JournalService journalService;

    @Autowired
    public GoalControllerTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ){
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
        List<Objective> mockObjectives = new ArrayList<>();
        List<Progress> mockProgress = new ArrayList<>();

        mockProgress.add(
                Progress.builder()
                        .recKey("a")
                        .compareType("=")
                        .targetValue((double)1)
                        .build()
        );

        mockProgress.add(
                Progress.builder()
                        .recKey("b")
                        .compareType("<")
                        .targetValue((double)2)
                        .build()
        );

        Objective mockObjective1 = Objective.builder()
                .topic("test")
                .description("Test this goal")
                .progressList(mockProgress)
                .build();


        mockObjectives.add(mockObjective1);

        mockGoal = Goal.builder()
                .assumptions("This is a unit test for Goal Controller...")
                .icon("Some/Location.png")
                .gains("Goal endpoints will work properly...")
                .description("Verify that Goal endpoints work as expected.")
                .objectives(mockObjectives)
                .build();

        expectedSaveSuccessResult= "{\"id\":\"" + MOCK_SAVE_UUID_STRING + "\"}";


        when(goalTrackerService.saveGoal(any(UUID.class),any(Goal.class)))
                .thenReturn(Goal.builder()
                        .id(UUID.fromString(MOCK_SAVE_UUID_STRING))
                        .build()
                );

        when(journalService.ownsJournal(anyString(),any(UUID.class)))
               .thenReturn(true);
    }

    @DisplayName("Should pass validation and return UUID")
    @Test
    public void publishGoalSuccess_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "",
                "",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }


    @DisplayName("Should pass when icon is missing and return UUID")
    @Test
    public void publishGoalMissingIcon_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"icon\":\"Some/Location.png\",",
                "",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @DisplayName("Should pass when assumptions are missing and return UUID")
    @Test
    public void publishGoalMissingAssumptions_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"assumptions\":\"This is a unit test for Goal Controller...\",",
                "",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    @DisplayName("Should fail when description is missing")
    public void publicGoalMissingDescription_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"description\":\"Verify that Goal endpoints work as expected.\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should pass when gains are missing and return UUID")
    @Test
    public void publishGoalMissingGains_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"gains\":\"Goal endpoints will work properly...\",",
                "",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @DisplayName("Should fail when objectives are missing and return UUID")
    @Test
    public void publishGoalMissingObjectives_UnitTest() throws Exception {
        String objectivesJson = objectMapper.writeValueAsString(mockGoal.getObjectives());

        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"objectives\":"+objectivesJson,
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should pass when objectives are empty and return UUID")
    @Test
    public void publishGoalEmptyObjectives_UnitTest() throws Exception {
        String objectivesJson = objectMapper.writeValueAsString(mockGoal.getObjectives());

        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"objectives\":"+objectivesJson,
                "\"objectives\":[]",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @DisplayName("Should fail when topic is missing in an objective")
    @Test
    public void publishGoalMissingObjectiveTopic_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"topic\":\"test\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when description is missing in an objective")
    @Test
    public void publishGoalMissingObjectiveDescription_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"description\":\"Test this goal\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should fail when progress list is missing in an objective")
    @Test
    public void publishGoalMissingObjectiveProgress_UnitTest() throws Exception {
        Objective mockObjective = mockGoal.getObjectives().get(0);
        String progressListJson = objectMapper.writeValueAsString(mockObjective.getProgressList());
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"measurableTasks\":"+progressListJson,
                "",
                APIResult.FAIL);
    }


    @DisplayName("Should pass when progress list is empty in an objective")
    @Test
    public void publishGoalEmptyObjectiveProgress_UnitTest() throws Exception {
        Objective mockObjective = mockGoal.getObjectives().get(0);
        String objectiveJson= objectMapper.writeValueAsString(mockObjective);
        String progressListJson = objectMapper.writeValueAsString(mockObjective.getProgressList());

        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"measurableTasks\":"+progressListJson,
                "\"measurableTasks\":[]",
                APIResult.PASS);
        assertEquals(
                expectedSaveSuccessResult,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @DisplayName("Should pass when rec key is missing in a progress entry")
    @Test
    public void publishGoalMissingProgressRecKey_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"recKey\":\"a\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should pass when compare type is missing in a progress entry")
    @Test
    public void publishGoalMissingProgressCompareType_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"compareType\":\"=\",",
                "",
                APIResult.FAIL);
    }

    @DisplayName("Should pass when target value is missing in a progress entry")
    @Test
    public void publishGoalMissingProgressTargetValue_UnitTest() throws Exception {
        MvcResult mvcResult = genericAPITestUtil.doGenericPostValidationTest(
                mockGoal,
                "\"targetValue\":1.0",
                "",
                APIResult.FAIL);
    }
}
