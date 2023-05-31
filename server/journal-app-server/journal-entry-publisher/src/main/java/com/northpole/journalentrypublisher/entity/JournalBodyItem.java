package com.northpole.journalentrypublisher.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalBodyItem {

    @NotEmpty
    @NotNull
    private String topic;

    @NotNull
    private String description;

    @NotNull
    @Valid
    private List<Record> recordList;

}
