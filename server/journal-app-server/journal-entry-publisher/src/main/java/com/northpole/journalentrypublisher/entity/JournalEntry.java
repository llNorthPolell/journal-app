package com.northpole.journalentrypublisher.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {

    private UUID entryId;

    @NotNull
    private Integer journal;

    private String summary;

    private String overview;

    private LocalDateTime dateOfEntry;

    private LocalDateTime creationTimestamp;

    private LocalDateTime lastUpdated;

    private List<JournalBodyItem> journalBodyItems;

}
