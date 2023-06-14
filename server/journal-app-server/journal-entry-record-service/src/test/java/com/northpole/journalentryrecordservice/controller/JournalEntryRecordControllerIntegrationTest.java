package com.northpole.journalentryrecordservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.common.entity.FlatRecord;
import com.northpole.common.entity.JournalBodyItem;
import com.northpole.common.entity.JournalEntry;
import com.northpole.common.entity.Record;
import com.northpole.journalentryrecordservice.repository.FlatRecordRepository;
import com.northpole.journalentryrecordservice.service.JournalEntryRecordService;
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
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


@SpringBootTest
@Testcontainers
public class JournalEntryRecordControllerIntegrationTest {
    @Container
    private static KafkaContainer kafka= new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.2"));

    @Container
    private static MongoDBContainer mongodb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    private JournalEntryRecordService journalEntryRecordService;
    private ObjectMapper objectMapper;
    private KafkaTemplate<String,String> kafkaTemplate;

    private FlatRecordRepository flatRecordRepository;

    private JournalEntry mockJournalEntry;

    @Value("${northpole.kafka.topic.journal-entry}")
    private String testTopic;

    @Autowired
    public JournalEntryRecordControllerIntegrationTest(
            JournalEntryRecordService journalEntryRecordService,
            ObjectMapper objectMapper,
            KafkaTemplate<String,String> kafkaTemplate,
            FlatRecordRepository flatRecordRepository
    ){
        this.journalEntryRecordService = journalEntryRecordService;
        this.objectMapper=objectMapper;
        this.kafkaTemplate=kafkaTemplate;
        this.flatRecordRepository=flatRecordRepository;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers",kafka::getBootstrapServers);
        registry.add("spring.data.mongodb.uri",mongodb::getReplicaSetUrl);
    }

    @BeforeEach
    public void setupEachTest (){
        Record rec1 = Record.builder()
                .recKey("a")
                .recValue("1")
                .build();

        Record rec2 = Record.builder()
                .recKey("targetA")
                .recValue("2")
                .build();

        Record rec3 = Record.builder()
                .recKey("b")
                .recValue("asdfasdf")
                .build();

        Record rec4 = Record.builder()
                .recKey("c")
                .recValue("5")
                .build();

        Record rec5 = Record.builder()
                .recKey("d")
                .recValue("3")
                .build();

        List<Record> recList1 = new ArrayList<>();
        recList1.add(rec1);
        recList1.add(rec2);
        List<Record> recList2 = new ArrayList<>();
        recList2.add(rec3);
        recList2.add(rec4);
        recList2.add(rec5);

        JournalBodyItem body1 = JournalBodyItem.builder()
                .topic("My first topic")
                .description("My first topic ever!!!")
                .recordList(recList1)
                .build();
        JournalBodyItem body2 = JournalBodyItem.builder()
                .topic("Second")
                .description("This is my second topic...")
                .recordList(recList2)
                .build();
        JournalBodyItem body3 = JournalBodyItem.builder()
                .topic("Second")
                .description("This is my third topic.")
                .recordList(new ArrayList<>())
                .build();

        List<JournalBodyItem> bodyList = new ArrayList<>();
        bodyList.add(body1);
        bodyList.add(body2);
        bodyList.add(body3);

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
    @DisplayName("Should take JSON from Kafka, extract records, and save to data source")
    public void saveSuccess_IntegrationTest() throws JsonProcessingException {
        String output = objectMapper.writeValueAsString(mockJournalEntry);
        kafkaTemplate.send(testTopic, output);

        await().pollInterval(Duration.ofSeconds(3)).atMost(10, TimeUnit.SECONDS).untilAsserted(()->{
            List<FlatRecord> searchResult = flatRecordRepository.findAll();
            assertTrue(!searchResult.isEmpty());
        });

    }


}
