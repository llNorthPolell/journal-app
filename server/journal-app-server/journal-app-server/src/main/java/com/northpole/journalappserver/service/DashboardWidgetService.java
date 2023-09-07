package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.DashboardWidget;

import java.util.List;
import java.util.UUID;

public interface DashboardWidgetService {

    DashboardWidget createDashboardWidget(UUID journalRef,DashboardWidget payload);

    List<DashboardWidget> getDashboardWidgetData(UUID journalRef);

    DashboardWidget updateDashboardWidget(UUID journalRef,DashboardWidget payload);

    boolean ownsWidget(UUID journalRef, int widgetId);
}
