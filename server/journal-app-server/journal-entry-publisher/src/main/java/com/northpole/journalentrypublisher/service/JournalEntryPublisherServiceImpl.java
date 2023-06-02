package com.northpole.journalentrypublisher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.common.entity.JournalEntry;
import com.northpole.journalentrypublisher.config.KafkaConfigProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class JournalEntryPublisherServiceImpl implements JournalEntryPublisherService{

    private KafkaConfigProps kafkaConfigProps;

    private KafkaTemplate<String,String> kafkaTemplate;

    private ObjectMapper objectMapper;

    @Autowired
    public JournalEntryPublisherServiceImpl (
            KafkaConfigProps kafkaConfigProps,
            KafkaTemplate<String,String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaConfigProps = kafkaConfigProps;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public UUID processJournalEntry(JournalEntry payload) {
        // generate UUID and assign to journal entry
        UUID saveId = UUID.randomUUID();
        payload.setEntryId(saveId);

        try {
            String output = objectMapper.writeValueAsString(payload);

            // send message to topic
            kafkaTemplate.send(kafkaConfigProps.getTopic(), output);

            return saveId;
        }
        catch(JsonProcessingException e){
            System.out.println("Failed to save Journal Entry...");
            return null;
        }
    }
}
