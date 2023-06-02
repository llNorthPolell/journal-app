package com.northpole.common.entity;

import jakarta.validation.Valid;
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
