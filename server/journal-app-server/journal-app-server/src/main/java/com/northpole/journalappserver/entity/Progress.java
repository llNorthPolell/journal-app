package com.northpole.journalappserver.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotNull
    @Column(name="rec_key")
    private String recKey;

    @Column(name="current_value")
    private double currentValue;

    @Column(name="compare_type")
    private String compareType;

    @NotNull
    @Column(name="target_value")
    private double targetValue;

    @Column (name="last_checked_entry_date")
    private LocalDateTime entryDateLastChecked;
}
