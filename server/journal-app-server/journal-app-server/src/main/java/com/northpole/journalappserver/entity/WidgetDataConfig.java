package com.northpole.journalappserver.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="widget_data_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WidgetDataConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotNull
    @Column(name="type")
    private String type;

    @NotNull
    @Column(name="label")
    private String label;

    @Column(name="color")
    @Pattern(regexp = "#[a-fA-F0-9]{6}")
    private String color;

    @NotNull
    @Column(name="rule")
    private String rule;
}
