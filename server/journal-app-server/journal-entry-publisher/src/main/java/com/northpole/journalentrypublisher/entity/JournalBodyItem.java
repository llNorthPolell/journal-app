package com.northpole.journalentrypublisher.entity;

import jakarta.validation.constraints.NotEmpty;
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

    private String topic;

    private String description;

    private List<Record> recordList;

}
