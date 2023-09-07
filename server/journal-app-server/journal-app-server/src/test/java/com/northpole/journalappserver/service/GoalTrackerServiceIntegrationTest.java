package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.northpole.journalappserver.entity.Goal;
import com.northpole.journalappserver.entity.Objective;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/*
This test class is required for testing trigger function in Postgres that can not be covered by repository alone.
 */
@SpringBootTest
@Testcontainers
public class GoalTrackerServiceIntegrationTest {
    @Container
    private static PostgreSQLContainer postgresDB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withInitScript("testdbinitsql/test_db2.sql");

    @Container
    private static MongoDBContainer mongodb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");

    private GoalTrackerService goalTrackerService;

    private ObjectiveRepository objectiveRepository;

    private GoalRepository goalRepository;

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
        goalRepository.save(
                Goal.builder()
                        .id(UUID.fromString("e6dc531c-2427-46bd-a7c6-748f86fd33ae"))
                        .description("Get Goal By Journal ID Works")
                        .journal(MOCK_JOURNAL_REF)
                        .build()
        );
    }

    @AfterEach
    public void cleanupAfterEachTest(){
        goalRepository.deleteAll();
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
                            "recKey": "Tasks Tested",
                            "recValue": "2"
                        }
                    ]
                }
                """;
        try {
            String response = goalTrackerService.updateProgress(message);
            assertEquals("{\"objectives\":[7]}", response);

            List<Objective> allObjectives = objectiveRepository.findAllByJournalId(3);
            Optional<Objective> result = objectiveRepository.findById(7);

            assertTrue(result.isPresent());
            assertEquals("COMPLETE", result.get().getStatus());
            assertEquals(LocalDateTime.parse("2022-08-19T00:00:00", formatter), result.get().getDateCompleted());
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
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
                            "journal": "e958ac56-2f12-4d35-ba8e-979aca28b4a6",
                            "topic": "test3",
                            "recKey": "Goals Created",
                            "recValue": "1"
                        },
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a2d",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": "e958ac56-2f12-4d35-ba8e-979aca28b4a6",
                            "topic": "test3",
                            "recKey": "Tasks Tested",
                            "recValue": "1"
                        }
                    ]
                }
                """;

        try {
            String response = goalTrackerService.updateProgress(message);
            assertEquals("{\"objectives\":[9]}", response);

            List<Objective> allObjectives = objectiveRepository.findAllByJournalId(3);
            Optional<Objective> result = objectiveRepository.findById(9);

            assertTrue(result.isPresent());
            assertEquals("IN PROGRESS", result.get().getStatus());
            assertNull(result.get().getDateCompleted());
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
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
                            "journal": "e958ac56-2f12-4d35-ba8e-979aca28b4a6",
                            "topic": "test2",
                            "recKey": "Goals Created",
                            "recValue": "1"
                        },
                        {
                            "id": "a5e6346d-9967-4e23-b6b5-baaa9e619a2d",
                            "dateOfEntry": "2022-08-19T00:00:00",
                            "journal": "e958ac56-2f12-4d35-ba8e-979aca28b4a6",
                            "topic": "test2",
                            "recKey": "Tasks Tested",
                            "recValue": "1"
                        }
                    ]
                }
                """;

        try {
            String response = goalTrackerService.updateProgress(message);
            assertEquals("{\"objectives\":[8]}", response);

            List<Objective> allObjectives = objectiveRepository.findAllByJournalId(3);
            Optional<Objective> result = objectiveRepository.findById(8);

            assertTrue(result.isPresent());
            assertEquals("COMPLETE", result.get().getStatus());
            assertEquals(LocalDateTime.parse("2022-08-19T00:00:00", formatter), result.get().getDateCompleted());
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

}
