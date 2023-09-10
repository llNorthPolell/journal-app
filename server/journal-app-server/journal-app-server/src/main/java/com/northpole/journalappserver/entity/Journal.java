package com.northpole.journalappserver.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="journal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @JsonIgnore
    private Integer journalId;

    @JsonProperty("id")
    @Column(name="journal_ref")
    private UUID journalRef;

    @NotNull
    @Column(name="name")
    private String name;

    @Column(name="author")
    @JsonIgnore
    private String author;

    @Column(name="creation_timestamp")
    private LocalDateTime creationTimestamp;

    @Column(name="last_updated")
    private LocalDateTime lastUpdated;

    @Column(name="img")
    private String img;



}
