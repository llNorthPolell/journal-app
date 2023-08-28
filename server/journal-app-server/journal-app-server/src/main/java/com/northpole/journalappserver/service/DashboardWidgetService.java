package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.DashboardWidget;

import java.util.List;

public interface DashboardWidgetService {
    List<DashboardWidget> getDashboardWidgetData(int journalId);
}
