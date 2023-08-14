package com.northpole.journalappserver.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryRecordServiceInput {
    @NotNull
    private int journal;

    private String topic;

    private String recKey;

    private String recKeyX;

    private String recKeyY;
}
