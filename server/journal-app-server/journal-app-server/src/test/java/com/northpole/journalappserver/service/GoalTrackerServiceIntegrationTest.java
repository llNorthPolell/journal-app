package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.Goal;
import com.northpole.journalappserver.entity.Objective;
import com.northpole.journalappserver.entity.Progress;
import com.northpole.journalappserver.repository.GoalRepository;
import com.northpole.journalappserver.repository.ObjectiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class GoalTrackerServiceIntegrationTest {
    @Container
    private static PostgreSQLContainer postgresDB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withInitScript("testdbinitsql/test_db2.sql");

    @Container
    private static MongoDBContainer mongodb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    private GoalTrackerService goalTrackerService;

    private ObjectiveRepository objectiveRepository;

    private GoalRepository goalRepository;

    private Goal mockGoal;

    @Autowired
    public GoalTrackerServiceIntegrationTest(
            GoalTrackerService goalTrackerService,
            GoalRepository goalRepository,
            ObjectiveRepository objectiveRepository
    ){
        this.goalTrackerService=goalTrackerService;
        this.goalRepository=goalRepository;
        this.objectiveRepository=objectiveRepository;
    }
    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        // MongoDB
        registry.add("spring.data.mongodb.uri",mongodb::getReplicaSetUrl);
        // Postgres
        registry.add("spring.datasource.url",postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username",postgresDB::getUsername);
        registry.add("spring.datasource.password",postgresDB::getPassword);

    }

    @BeforeEach
    public void setupBeforeEachTest(){
        List<Objective> mockObjectives = new ArrayList<>();
        List<Progress> mockProgress = new ArrayList<>();

        mockProgress.add(
                Progress.builder()
                        .recKey("Goals Created")
                        .compareType("=")
                        .targetValue((double)1)
                        .build()
        );

        Objective mockObjective1 = Objective.builder()
                .topic("test")
                .description("Create one goal")
                .progressList(mockProgress)
                .build();


        mockObjectives.add(mockObjective1);

        mockGoal = Goal.builder()
                .journal(1)
                .assumptions("This is a integration test for save...")
                .icon("Some/Location.png")
                .gains("Integration Test for Saving Goals Pass...")
                .description("Test if goals can be created and saved in MongoDB and tracked with Postgres")
                .objectives(mockObjectives)
                .build();

        goalRepository.save(
                Goal.builder()
                        .id(UUID.fromString("e6dc531c-2427-46bd-a7c6-748f86fd33ae"))
                        .description("Get Goal By Journal ID Works")
                        .journal(3)
                        .build()
        );
    }

    @AfterEach
    public void cleanupAfterEachTest(){
        goalRepository.deleteAll();
    }


    @Test
    @DisplayName("Should save entire Goal object to MongoDB, Objectives and Progress to Postgres")
    public void saveSuccess_IntegrationTest(){
        GeneralResponseBody response = goalTrackerService.saveGoal(mockGoal);
        assertEquals(200,response.getStatus());

        Optional<Objective> newObjective = objectiveRepository.findById(1);
        List<Goal> allGoals = goalRepository.findAll();
        Goal newGoal = allGoals.get(1);

        assertEquals("{\"goalId\":\"" + newGoal.getId()
                + "\", \"objectiveIds\":["+newObjective.get().getId()+"]}",response.getMessage());


        assertTrue(newObjective.isPresent());
        assertEquals("Create one goal", newObjective.get().getDescription());
        assertNotNull(newGoal);
        assertEquals("Test if goals can be created and saved in MongoDB and tracked with Postgres",
                newGoal.getDescription());
    }


    @Test
    @DisplayName("Should update Objective to COMPLETE status when both tasks(progress entries) have met target_value")
    public void updateProgress_compareTypeAND_completed_IntegrationTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
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
                            "recKey": "Tasks Tested",
                            "recValue": "2"
                        }
                    ]
                }
                """;

        GeneralResponseBody response = goalTrackerService.updateProgress(message);
        assertEquals(200,response.getStatus());

        List<Objective> allObjectives = objectiveRepository.findAllByJournalId(3);
        Optional<Objective> result = objectiveRepository.findById(7);

        assertTrue(result.isPresent());
        assertEquals("COMPLETE",result.get().getStatus());
        assertEquals(LocalDateTime.parse("2022-08-19T00:00:00",formatter), result.get().getDateCompleted());
    }

    @Test
    @DisplayName("Should not update Objective to COMPLETE status when only one task(progress entry) meets target_value")
    public void updateProgress_compareTypeAND_notCompleted_IntegrationTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String message = """
                {
                    "count": 2,
                    "flatRecords": [
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a1c",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": 3,
                            "topic": "test3",
                            "recKey": "Goals Created",
                            "recValue": "1"
                        },
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a2d",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": 3,
                            "topic": "test3",
                            "recKey": "Tasks Tested",
                            "recValue": "1"
                        }
                    ]
                }
                """;


        GeneralResponseBody response = goalTrackerService.updateProgress(message);
        assertEquals(200,response.getStatus());

        List<Objective> allObjectives = objectiveRepository.findAllByJournalId(3);
        Optional<Objective> result = objectiveRepository.findById(9);

        assertTrue(result.isPresent());
        assertEquals("IN PROGRESS",result.get().getStatus());
        assertNull(result.get().getDateCompleted());
    }

    @Test
    @DisplayName("Should update Objective to COMPLETE status when any one task(progress entries) have met target_value")
    public void updateProgress_compareTypeOR_completed_IntegrationTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String message = """
                {
                    "count": 2,
                    "flatRecords": [
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a1c",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": 3,
                            "topic": "test2",
                            "recKey": "Goals Created",
                            "recValue": "1"
                        },
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a2d",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": 3,
                            "topic": "test2",
                            "recKey": "Tasks Tested",
                            "recValue": "1"
                        }
                    ]
                }
                """;

        GeneralResponseBody response = goalTrackerService.updateProgress(message);
        assertEquals(200,response.getStatus());

        List<Objective> allObjectives = objectiveRepository.findAllByJournalId(3);
        Optional<Objective> result = objectiveRepository.findById(8);

        assertTrue(result.isPresent());
        assertEquals("COMPLETE",result.get().getStatus());
        assertEquals(LocalDateTime.parse("2022-08-19T00:00:00",formatter), result.get().getDateCompleted());
    }

    @Test
    @DisplayName("Should return consolidate Goals and Objectives for given journal ID as a list")
    public void getGoalsByJournal_IntegrationTest(){
        int journalId = 3;
        List<Goal> result = goalTrackerService.getGoalsWithProgressInJournal(journalId);

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals("Get Goal By Journal ID Works", result.get(0).getDescription());
        assertEquals(1, result.get(0).getObjectives().size());
        assertEquals(7, result.get(0).getObjectives().get(0).getId());
    }
}
