package com.northpole.journalappserver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlatRecord {
    @Id
    private UUID id;

    private LocalDateTime dateOfEntry;

    private UUID journal;

    private UUID journalEntry;

    private String topic;

    private String recKey;

    private String recValue;

}