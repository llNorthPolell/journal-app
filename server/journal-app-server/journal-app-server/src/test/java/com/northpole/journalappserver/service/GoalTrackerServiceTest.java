package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.Goal;
import com.northpole.journalappserver.entity.Objective;
import com.northpole.journalappserver.entity.Progress;
import com.northpole.journalappserver.repository.GoalRepository;
import com.northpole.journalappserver.repository.ObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GoalTrackerServiceTest {
    @Mock
    private GoalRepository goalRepository;

    @Mock
    private ObjectiveRepository objectiveRepository;

    private final UUID MOCK_GOAL_ID = UUID.fromString("9a8bac07-b2b0-47fc-a0fe-17966c3bde7b");

    private Goal mockGoal;

    @InjectMocks
    private GoalTrackerService goalTrackerService;

    @Autowired
    public GoalTrackerServiceTest(
            GoalRepository goalRepository,
            ObjectiveRepository objectiveRepository,
            GoalTrackerService goalTrackerService
    ){
        this.goalRepository=goalRepository;
        this.objectiveRepository=objectiveRepository;
        this.goalTrackerService=goalTrackerService;
    }


    @BeforeEach
    public void setupEachTest(){
        List<Objective> mockObjectives = new ArrayList<>();
        List<Progress> mockProgress = new ArrayList<>();
        List<Objective> mockSaveObjectiveResult = new ArrayList<>();

        mockProgress.add(
                Progress.builder()
                        .recKey("Goals Created")
                        .compareType(">=")
                        .targetValue(1)
                        .build()
        );

        Objective mockObjective1 = Objective.builder()
                .topic("test")
                .goalId(MOCK_GOAL_ID)
                .description("Create one goal")
                .progressList(mockProgress)
                .build();

        mockObjectives.add(mockObjective1);

        mockSaveObjectiveResult.add(
                Objective.builder()
                        .topic(mockObjective1.getTopic())
                        .goalId(mockObjective1.getGoalId())
                        .description(mockObjective1.getDescription())
                        .progressList(mockObjective1.getProgressList())
                        .id(1)
                        .build()
        );

        mockGoal = Goal.builder()
                .journal(3)
                .assumptions("TESTJournal was created...")
                .icon("Some/Location.png")
                .gains("Goals can be created and Goal Tracking can proceed...")
                .description("Test if goals can be created and saved in MongoDB and tracked with Postgres")
                .objectives(mockObjectives)
                .build();

        when(goalRepository.save(any(Goal.class))).thenReturn(
                Goal.builder()
                        .id(MOCK_GOAL_ID)
                        .journal(mockGoal.getJournal())
                        .assumptions(mockGoal.getAssumptions())
                        .icon(mockGoal.getIcon())
                        .gains(mockGoal.getGains())
                        .description(mockGoal.getDescription())
                        .objectives(mockGoal.getObjectives())
                        .build()
        );

        when(objectiveRepository.saveAll(any(List.class))).thenReturn(
                mockSaveObjectiveResult
        );

        ReflectionTestUtils.setField(goalTrackerService, "goalRepository", goalRepository);
        ReflectionTestUtils.setField(goalTrackerService, "objectiveRepository", objectiveRepository);
    }

    @Test
    @DisplayName("Should run GoalRepository.save and ObjectiveRepository.save")
    public void SaveGoal_UnitTest(){
        GeneralResponseBody result = goalTrackerService.saveGoal(mockGoal);

        verify(goalRepository,times(1)).save(any(Goal.class));
        verify(objectiveRepository,times(1)).saveAll(any(List.class));


        assertEquals(200,result.getStatus());
        assertEquals("{\"goalId\":\"" + MOCK_GOAL_ID.toString() + "\", \"objectiveIds\":"+"[1]"+"}", result.getMessage());
    }


    @Test
    @DisplayName("Should update progress when a journal entry is created")
    public void UpdateProgress_UnitTest(){
        String message = """
                {
                    "count": 2,
                    "flatRecords": [
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a1c",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": 3,
                            "topic": "test",
                            "recKey": "Goals Created",
                            "recValue": "1"
                        },
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a2d",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": 3,
                            "topic": "test",
                            "recKey": "Tested",
                            "recValue": "2"
                        }
                    ]
                }
                """;
        GeneralResponseBody result = goalTrackerService.updateProgress(message);

        assertEquals(200,result.getStatus());
        verify(objectiveRepository,times(1)).findAllByJournalIdIsAndTopicInAndStatusIs(
                anyInt(),anySet(), anyString()
        );
        verify(objectiveRepository,times(1)).saveAll(any(List.class));
    }

}
