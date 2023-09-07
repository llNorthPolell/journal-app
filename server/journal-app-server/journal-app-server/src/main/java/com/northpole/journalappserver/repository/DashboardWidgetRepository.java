package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.DashboardWidget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface DashboardWidgetRepository extends JpaRepository<DashboardWidget, Integer> {
    List<DashboardWidget> getDashboardWidgetsByJournal(@Param("journal") int journalId);
}
