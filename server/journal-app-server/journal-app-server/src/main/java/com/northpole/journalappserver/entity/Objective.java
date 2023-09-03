package com.northpole.journalappserver.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="objective")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Objective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="goal")
    private UUID goalId;

    @Column(name="journal")
    private int journalId;

    @Column(name="status")
    private String status;

    @NotNull
    @Column(name="topic")
    private String topic;

    @NotNull
    @Column(name="description")
    private String description;

    @Column(name="deadline")
    private LocalDateTime deadline;

    @Column(name="date_completed")
    private LocalDateTime dateCompleted;

    @Column(name="creation_timestamp")
    private LocalDateTime creationTimestamp;

    @Column(name="last_updated")
    private LocalDateTime lastUpdated;

    @Column(name="completion_criteria")
    private String completionCriteria;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="objective", nullable = false)
    private List<Progress> progressList;
}
