package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Goal;
import com.northpole.journalappserver.entity.Objective;
import com.northpole.journalappserver.entity.Progress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class GoalRepositoryIntegrationTest {

    @Container
    private static MongoDBContainer mongodb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");
    private final UUID MOCK_NEW_GOAL_ID=UUID.fromString("e6dc531c-2427-46bd-a7c6-748f86fd33ae");
    private final UUID EXISTING_GOAL_ID=UUID.fromString("ed49b4b7-098c-462d-9ede-b249ae5b1f00");

    private GoalRepository goalRepository;

    private Goal mockGoal;


    @Autowired
    public GoalRepositoryIntegrationTest(
            GoalRepository goalRepository
    ) {
        this.goalRepository = goalRepository;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri",mongodb::getReplicaSetUrl);
    }

    @BeforeEach
    public void setupBeforeEachTest(){
        mockGoal = Goal.builder()
                .id(MOCK_NEW_GOAL_ID)
                .journal(MOCK_JOURNAL_REF)
                .assumptions("This is a integration test for save...")
                .icon("Some/Location.png")
                .gains("Integration Test for Saving Goals Pass...")
                .description("Test if goals can be created and saved in MongoDB and tracked with Postgres")
                .build();

        goalRepository.save(
                Goal.builder()
                        .id(EXISTING_GOAL_ID)
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
    @DisplayName("Should save Goal object to MongoDB")
    public void saveSuccess_IntegrationTest(){
        Goal saveResult = goalRepository.save(mockGoal);

        assertNotNull(saveResult);

        assertEquals(2,goalRepository.count());

        Optional<Goal> findNewGoalResult = goalRepository.findById(saveResult.getId());
        assertTrue(findNewGoalResult.isPresent());

        Goal newGoal = findNewGoalResult.get();
        assertEquals(mockGoal.getId(),newGoal.getId());
        assertEquals(mockGoal.getDescription(), newGoal.getDescription());
        assertEquals(mockGoal.getIcon(), newGoal.getIcon());
        assertEquals(mockGoal.getAssumptions(), newGoal.getAssumptions());
        assertEquals(mockGoal.getGains(), newGoal.getGains());
        assertEquals(mockGoal.getJournal(), newGoal.getJournal());
    }


    @Test
    @DisplayName("Should return consolidate Goals and Objectives for given journal ID as a list")
    public void getGoalsByJournal_IntegrationTest(){
        List<Goal> result = goalRepository.findAllByJournal(MOCK_JOURNAL_REF);

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals("Get Goal By Journal ID Works", result.get(0).getDescription());
    }
}
