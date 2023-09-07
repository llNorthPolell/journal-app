package com.northpole.journalappserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

// TODO: MongoDB or Postgres?
@Document("goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    private UUID id;

    @JsonIgnore
    private UUID journal;

    private String icon;

    @NotNull
    private String description;

    private String assumptions;

    private String gains;

    private String status;

    @NotNull
    @Valid
    private List<Objective> objectives;

}
