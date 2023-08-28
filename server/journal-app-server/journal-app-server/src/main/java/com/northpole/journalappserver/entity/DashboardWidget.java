package com.northpole.journalappserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="dashboard_widget")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardWidget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="journal")
    private int journal;

    @Column(name="type")
    private String type;

    @Column(name="creation_timestamp")
    private LocalDateTime creationTimestamp;

    @Column(name="last_updated")
    private LocalDateTime lastUpdated;

    @Column(name="position")
    private int position;

    @Column(name="title")
    private String title;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="widget")
    private List<WidgetDataConfig> configs;

    @Transient
    private DashboardWidgetPayload payload;

}
