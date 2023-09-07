package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GoalTrackerServiceTest {
    @Mock
    private JournalService journalService;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private ObjectiveRepository objectiveRepository;

    private final int MOCK_JOURNAL_ID=3;
    private final UUID MOCK_JOURNAL_REF = UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");
    private final UUID MOCK_GOAL_ID = UUID.fromString("9a8bac07-b2b0-47fc-a0fe-17966c3bde7b");

    private Goal mockGoal;
    private List<Objective> mockObjectives = new ArrayList<>();

    @InjectMocks
    private GoalTrackerService goalTrackerService;

    @Autowired
    public GoalTrackerServiceTest(
            JournalService journalService,
            GoalRepository goalRepository,
            ObjectiveRepository objectiveRepository,
            GoalTrackerService goalTrackerService
    ){
        this.journalService=journalService;
        this.goalRepository=goalRepository;
        this.objectiveRepository=objectiveRepository;
        this.goalTrackerService=goalTrackerService;
    }


    @BeforeEach
    public void setupEachTest(){
        List<Progress> mockProgress = new ArrayList<>();
        List<Objective> mockSaveObjectiveResult = new ArrayList<>();

        mockProgress.add(
                Progress.builder()
                        .recKey("Goals Created")
                        .compareType(">=")
                        .targetValue((double)1)
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
                        .journalId(MOCK_JOURNAL_ID)
                        .topic(mockObjective1.getTopic())
                        .goalId(mockObjective1.getGoalId())
                        .description(mockObjective1.getDescription())
                        .progressList(mockObjective1.getProgressList())
                        .id(1)
                        .build()
        );

        mockGoal = Goal.builder()
                .assumptions("TESTJournal was created...")
                .icon("Some/Location.png")
                .gains("Goals can be created and Goal Tracking can proceed...")
                .description("Test if goals can be created and saved in MongoDB and tracked with Postgres")
                .objectives(mockObjectives)
                .build();

        when(goalRepository.save(any(Goal.class))).thenReturn(
                Goal.builder()
                        .id(MOCK_GOAL_ID)
                        .journal(MOCK_JOURNAL_REF)
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

        when(journalService.getJournalId(any(UUID.class)))
                .thenReturn(MOCK_JOURNAL_ID);

        ReflectionTestUtils.setField(goalTrackerService, "journalService", journalService);
        ReflectionTestUtils.setField(goalTrackerService, "goalRepository", goalRepository);
        ReflectionTestUtils.setField(goalTrackerService, "objectiveRepository", objectiveRepository);
    }

    @Test
    @DisplayName("Should run GoalRepository.save and ObjectiveRepository.save")
    public void SaveGoal_UnitTest(){
        Goal result = goalTrackerService.saveGoal(MOCK_JOURNAL_REF,mockGoal);

        verify(goalRepository,times(1)).save(any(Goal.class));
        verify(objectiveRepository,times(1)).saveAll(any(List.class));

        assertEquals(MOCK_JOURNAL_REF,result.getJournal());
        assertEquals(mockGoal.getAssumptions(),result.getAssumptions());
        assertEquals(mockGoal.getGains(),result.getGains());
        assertEquals(mockGoal.getDescription(),result.getDescription());
        assertEquals(mockGoal.getIcon(),result.getIcon());
        assertEquals(mockObjectives.size(),result.getObjectives().size());

        Objective mockObjective = mockObjectives.get(0);
        Objective newObjective = result.getObjectives().get(0);


        assertEquals(MOCK_GOAL_ID,newObjective.getGoalId());
        assertEquals(MOCK_JOURNAL_ID,newObjective.getJournalId());
        assertEquals(mockObjective.getDescription(),newObjective.getDescription());
        assertEquals(mockObjective.getTopic(),newObjective.getTopic());
        assertEquals(mockObjective.getProgressList().size(),newObjective.getProgressList().size());

        Progress mockProgress = mockObjective.getProgressList().get(0);
        Progress newProgress = newObjective.getProgressList().get(0);

        assertEquals(0,newProgress.getCurrentValue());
        assertEquals(mockProgress.getRecKey(),newProgress.getRecKey());
        assertEquals(mockProgress.getCompareType(),newProgress.getCompareType());
        assertEquals(mockProgress.getTargetValue(),newProgress.getTargetValue());

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
                            "journal": "e958ac56-2f12-4d35-ba8e-979aca28b4a6",
                            "topic": "test",
                            "recKey": "Goals Created",
                            "recValue": "1"
                        },
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a2d",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": "e958ac56-2f12-4d35-ba8e-979aca28b4a6",
                            "topic": "test",
                            "recKey": "Tested",
                            "recValue": "2"
                        }
                    ]
                }
                """;
        try {
            String result = goalTrackerService.updateProgress(message);

            assertEquals("{\"objectives\":[1]}", result);
            verify(objectiveRepository, times(1)).findAllByJournalIdIsAndTopicInAndStatusIs(
                    anyInt(), anySet(), anyString()
            );
            verify(objectiveRepository, times(1)).saveAll(any(List.class));
        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }
    }

}
