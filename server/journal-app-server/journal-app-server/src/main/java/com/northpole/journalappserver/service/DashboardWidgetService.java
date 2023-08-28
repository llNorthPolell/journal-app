package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.GeneralResponseBody;

import java.util.List;

public interface DashboardWidgetService {
    List<DashboardWidget> getDashboardWidgetData(int journalId);

    GeneralResponseBody createDashboardWidget(DashboardWidget payload);
}
