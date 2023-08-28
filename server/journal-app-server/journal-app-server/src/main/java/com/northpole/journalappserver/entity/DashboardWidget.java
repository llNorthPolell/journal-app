package com.northpole.journalappserver.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    private Integer id;

    @Column(name="journal")
    @NotNull
    private Integer journal;

    @Column(name="type")
    @NotNull
    private String type;

    @Column(name="creation_timestamp")
    private LocalDateTime creationTimestamp;

    @Column(name="last_updated")
    private LocalDateTime lastUpdated;

    @Column(name="position")
    private Integer position;

    @Column(name="title")
    @NotNull
    private String title;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="widget")
    @Valid
    private List<WidgetDataConfig> configs;

    @Transient
    private DashboardWidgetPayload payload;

}
