package com.northpole.journalentrypersist.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.common.entity.JournalBodyItem;
import com.northpole.common.entity.JournalEntry;
import com.northpole.common.entity.Record;
import com.northpole.journalentrypersist.repository.JournalEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
public class JournalEntryPersistControllerIntegrationTest {

    @Container
    private static KafkaContainer kafka= new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.2"));

    private KafkaTemplate<String,String> kafkaTemplate;

    private ObjectMapper objectMapper;

    private JournalEntry mockJournalEntry;

    private JournalEntryRepository journalEntryRepository;

    @Value("${northpole.kafka.topic}")
    private String testTopic;

    @Autowired
    public JournalEntryPersistControllerIntegrationTest(
            KafkaTemplate<String,String> kafkaTemplate,
            ObjectMapper objectMapper,
            JournalEntryRepository journalEntryRepository
    ){

        this.kafkaTemplate=kafkaTemplate;
        this.objectMapper=objectMapper;
        this.journalEntryRepository=journalEntryRepository;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers",kafka::getBootstrapServers);
    }



    @BeforeEach
    public void setupBeforeEachTest(){
        Record rec1 = Record.builder()
                .recKey("a")
                .recValue("1")
                .build();

        Record rec2 = Record.builder()
                .recKey("targetA")
                .recValue("2")
                .build();
        List<Record> recList1 = new ArrayList<>();
        recList1.add(rec1);
        recList1.add(rec2);

        JournalBodyItem body1 = JournalBodyItem.builder()
                .topic("My first topic")
                .description("My first topic ever!!!")
                .recordList(recList1)
                .build();
        JournalBodyItem body2 = JournalBodyItem.builder()
                .topic("Second")
                .description("This is my second topic...")
                .recordList(new ArrayList<Record>())
                .build();

        List<JournalBodyItem> bodyList = new ArrayList<>();
        bodyList.add(body1);
        bodyList.add(body2);

        this.mockJournalEntry = JournalEntry.builder()
                .journal(3)
                .entryId(UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7b3e5c8"))
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
                .journalBodyItems(bodyList)
                .build();

    }


    @Test
    @DisplayName("Should successfully obtain JournalEntry object from Kafka and save to data source")
    public void saveSuccess_integrationTest() throws JsonProcessingException {
        String output = objectMapper.writeValueAsString(mockJournalEntry);
        kafkaTemplate.send(testTopic, output);

        await().pollInterval(Duration.ofSeconds(3)).atMost(10, TimeUnit.SECONDS).untilAsserted(()->{
            Optional<JournalEntry> searchResult = journalEntryRepository.findById(mockJournalEntry.getEntryId());
            assertNotEquals(Optional.empty(),searchResult);
        });

        journalEntryRepository.delete(mockJournalEntry);
    }
}
