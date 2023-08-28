package com.northpole.journalappserver.entity;

import jakarta.persistence.*;
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

    @Column(name="type")
    private String type;

    @Column(name="label")
    private String label;

    @Column(name="color")
    private String color;

    @Column(name="rule")
    private String rule;
}
