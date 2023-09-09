package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Journal;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class JournalRepositoryIntegrationTest {
    @Container
    private static PostgreSQLContainer postgresDB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withInitScript("testdbinitsql/test_db.sql");

    private JournalRepository journalRepository;

    private final UUID MOCK_NEW_JOURNAL_REF = UUID.fromString("f2775362-01d9-4918-a3d7-d9816d0ed3f7");
    private final UUID MOCK_EXISTING_JOURNAL_REF = UUID.fromString("ab8526d6-ccb8-4cd1-a6ff-6a8108f1a703");
    private final UUID MOCK_EXISTING_JOURNAL_REF2 = UUID.fromString("030029cb-8257-4396-85fb-c034286bb916");
    private final LocalDateTime MOCK_TIMESTAMP = LocalDateTime.now();
    private final String MOCK_USER = "ASD1F2SDF3W";

    @Autowired
    public JournalRepositoryIntegrationTest(
            JournalRepository journalRepository
    ){
        this.journalRepository=journalRepository;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username",postgresDB::getUsername);
        registry.add("spring.datasource.password",postgresDB::getPassword);
    }


    @Test
    @DisplayName("Should create a Journal in Data Source")
    public void createSuccess_IntegrationTest(){
        final long count = journalRepository.count();
        Journal mockPayload = Journal.builder()
                .journalRef(MOCK_NEW_JOURNAL_REF)
                .author(MOCK_USER)
                .name("Integration test for create")
                .img("create.png")
                .creationTimestamp(MOCK_TIMESTAMP)
                .lastUpdated(MOCK_TIMESTAMP)
                .build();

        Journal saveResult = journalRepository.save(mockPayload);

        assertNotNull(saveResult);

        long newCount = journalRepository.count();
        assertEquals(count+1,newCount);

        Optional<Journal> findNewJournalResult =
                journalRepository.findById(saveResult.getJournalId());

        assertTrue(findNewJournalResult.isPresent());

        Journal newJournal = findNewJournalResult.get();

        assertEquals(MOCK_NEW_JOURNAL_REF,newJournal.getJournalRef());
        assertEquals(MOCK_USER,newJournal.getAuthor());
        assertEquals(mockPayload.getName(),newJournal.getName());
        assertEquals(mockPayload.getImg(),newJournal.getImg());

    }

    @Test
    @DisplayName("Should update an existing Journal in Data Source")
    public void updateSuccess_IntegrationTest(){
        final long count = journalRepository.count();
        Journal mockUpdatePayload = Journal.builder()
                .journalRef(MOCK_EXISTING_JOURNAL_REF)
                .img("image/updated.png")
                .name("Integration test for update")
                .build();

        Journal journalToUpdate = journalRepository.findByJournalRef(MOCK_EXISTING_JOURNAL_REF);
        journalToUpdate.setName(mockUpdatePayload.getName());
        journalToUpdate.setImg(mockUpdatePayload.getImg());
        journalToUpdate.setLastUpdated(mockUpdatePayload.getLastUpdated());

        Journal saveResult = journalRepository.save(journalToUpdate);

        long newCount = journalRepository.count();
        assertEquals(count,newCount);

        Optional<Journal> findUpdatedJournalResult = journalRepository.findById(journalToUpdate.getJournalId());

        assertTrue(findUpdatedJournalResult.isPresent());

        Journal updatedJournal = findUpdatedJournalResult.get();

        assertEquals(mockUpdatePayload.getName(),updatedJournal.getName());
        assertEquals(mockUpdatePayload.getImg(),updatedJournal.getImg());

        assertEquals(journalToUpdate.getAuthor(),updatedJournal.getAuthor());
        assertEquals(journalToUpdate.getJournalRef(),updatedJournal.getJournalRef());
        assertEquals(journalToUpdate.getCreationTimestamp(),updatedJournal.getCreationTimestamp());
    }

    @Test
    @DisplayName("Should delete an existing Journal in Data Source")
    public void deleteSuccess_IntegrationTest(){
        final long count = journalRepository.count();
        final Journal deleteJournal = journalRepository.findByJournalRef(MOCK_EXISTING_JOURNAL_REF2);
        journalRepository.delete(deleteJournal);

        long newCount = journalRepository.count();
        assertEquals(count-1,newCount);

        Optional<Journal> findDeletedJournalResult = journalRepository.findById(deleteJournal.getJournalId());
        assertTrue(findDeletedJournalResult.isEmpty());

    }
}
