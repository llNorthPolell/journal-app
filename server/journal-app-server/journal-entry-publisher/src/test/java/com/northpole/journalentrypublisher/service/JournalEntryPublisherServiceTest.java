package com.northpole.journalentrypublisher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalentrypublisher.config.KafkaConfigProps;
import com.northpole.journalentrypublisher.entity.JournalBodyItem;
import com.northpole.journalentrypublisher.entity.JournalEntry;
import com.northpole.journalentrypublisher.entity.Record;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JournalEntryPublisherServiceTest {

    @Mock
    private KafkaConfigProps kafkaConfigProps;

    @Mock
    private KafkaTemplate<String,String> kafkaTemplate;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private JournalEntryPublisherServiceImpl journalEntryPublisherService;

    @Autowired
    public JournalEntryPublisherServiceTest(
            KafkaConfigProps kafkaConfigProps,
            KafkaTemplate<String,String> kafkaTemplate,
            ObjectMapper objectMapper){
        this.kafkaConfigProps=kafkaConfigProps;
        this.kafkaTemplate=kafkaTemplate;
        this.objectMapper=objectMapper;
    }


    @DisplayName("should return UUID and send to Kafka topic once")
    @Test
    void processJournalEntrySuccess() throws JsonProcessingException {
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
                .build();

        List<JournalBodyItem> bodyList = new ArrayList<>();
        bodyList.add(body1);
        bodyList.add(body2);

        JournalEntry payload = JournalEntry.builder()
                .journal(3)
                .summary("My First Post")
                .overview("Woohoo! My First Post!! test")
                .dateOfEntry(LocalDateTime.of(2022,8,19,0,0,0,0))
                .journalBodyItems(bodyList)
                .build();

        UUID mockUUID = UUID.fromString("7aa881bc-f6e1-4621-9325-c199d7b3e5c8");
        mockStatic(UUID.class);

        when(UUID.randomUUID()).thenReturn(mockUUID);
        when(kafkaConfigProps.getTopic()).thenReturn("journalEntry.published");
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(null);

        // verify output is equal to hypothetical UUID
        assertEquals(mockUUID,  journalEntryPublisherService.processJournalEntry(payload));

        // verify payload.entryId is equal to hypothetical UUID
        assertEquals(mockUUID,  payload.getEntryId());

        // verify code to send to Kafka topic was triggered
        verify(kafkaTemplate,times(1)).send(anyString(), anyString());
    }

}
