package com.northpole.journalappserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document("journalEntries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    private UUID entryId;

    @JsonIgnore
    private UUID journal;

    @NotNull
    @NotEmpty
    private String summary;

    @NotNull
    private String overview;

    @NotNull
    private LocalDateTime dateOfEntry;

    private LocalDateTime creationTimestamp;

    private LocalDateTime lastUpdated;

    @NotNull
    @Valid
    private List<JournalBodyItem> journalBodyItems;

}