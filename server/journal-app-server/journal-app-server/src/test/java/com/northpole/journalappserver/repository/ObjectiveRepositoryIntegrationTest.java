package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Goal;
import com.northpole.journalappserver.entity.Objective;
import com.northpole.journalappserver.entity.Progress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class ObjectiveRepositoryIntegrationTest {
    @Container
    private static PostgreSQLContainer postgresDB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withInitScript("testdbinitsql/test_db2.sql");

    private final int MOCK_JOURNAL_ID=3;
    private final UUID MOCK_GOAL_ID=UUID.fromString("e6dc531c-2427-46bd-a7c6-748f86fd33ae");
    private final LocalDateTime MOCK_TIMESTAMP = LocalDateTime.now();

    private ObjectiveRepository objectiveRepository;

    private Objective mockNewObjective;

    @Autowired
    public ObjectiveRepositoryIntegrationTest(
            ObjectiveRepository objectiveRepository
    ){
            this.objectiveRepository=objectiveRepository;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username",postgresDB::getUsername);
        registry.add("spring.datasource.password",postgresDB::getPassword);

    }

    @BeforeEach
    public void setupBeforeEachTest(){
        List<Progress> mockNewProgress = new ArrayList<>();

        mockNewProgress.add(
                Progress.builder()
                        .recKey("Goals Created")
                        .currentValue((double)0)
                        .compareType("=")
                        .targetValue((double)1)
                        .build()
        );

        mockNewProgress.add(
                Progress.builder()
                        .recKey("Progress Created")
                        .currentValue((double)0)
                        .compareType(">")
                        .targetValue((double)1)
                        .build()
        );

        mockNewObjective = Objective.builder()
                .topic("test")
                .description("Create one goal")
                .goalId(MOCK_GOAL_ID)
                .journalId(MOCK_JOURNAL_ID)
                .progressList(mockNewProgress)
                .creationTimestamp(MOCK_TIMESTAMP)
                .lastUpdated(MOCK_TIMESTAMP)
                .build();

    }


    @Test
    @DisplayName("Should save entire Objective to Postgres")
    public void saveSuccess_IntegrationTest(){
        Objective saveResult = objectiveRepository.save(mockNewObjective);

        assertEquals(objectiveRepository.count(),4);

        Optional<Objective> findNewObjectiveResult = objectiveRepository.findById(saveResult.getId());
        assertTrue(findNewObjectiveResult.isPresent());

        Objective newObjective= findNewObjectiveResult.get();
        assertEquals(mockNewObjective.getGoalId(),newObjective.getGoalId());
        assertEquals(mockNewObjective.getDescription(), newObjective.getDescription());
        assertEquals(mockNewObjective.getTopic(), newObjective.getTopic());
        assertEquals(mockNewObjective.getJournalId(),newObjective.getJournalId());
        //assertEquals(MOCK_TIMESTAMP,newObjective.getCreationTimestamp());
        //assertEquals(MOCK_TIMESTAMP,newObjective.getLastUpdated());

        assertEquals(mockNewObjective.getProgressList().size(),newObjective.getProgressList().size());


        Progress mockProgress1 = mockNewObjective.getProgressList().get(0);
        Progress mockProgress2 = mockNewObjective.getProgressList().get(1);
        Progress newProgress1= newObjective.getProgressList().get(0);
        Progress newProgress2= newObjective.getProgressList().get(1);

        assertEquals(mockProgress1.getRecKey(),newProgress1.getRecKey());
        assertEquals(mockProgress2.getRecKey(),newProgress2.getRecKey());

        assertEquals(mockProgress1.getCurrentValue(),newProgress1.getCurrentValue());
        assertEquals(mockProgress2.getCurrentValue(),newProgress2.getCurrentValue());

        assertEquals(mockProgress1.getCompareType(),newProgress1.getCompareType());
        assertEquals(mockProgress2.getCompareType(),newProgress2.getCompareType());

        assertEquals(mockProgress1.getTargetValue(),newProgress1.getTargetValue());
        assertEquals(mockProgress2.getTargetValue(),newProgress2.getTargetValue());


    }




}
