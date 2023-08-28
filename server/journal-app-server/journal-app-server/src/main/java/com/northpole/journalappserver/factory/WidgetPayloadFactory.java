package com.northpole.journalappserver.factory;

import com.northpole.journalappserver.entity.DashboardWidgetPayload;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.WidgetDataConfig;

import java.util.List;

public interface WidgetPayloadFactory {

    DashboardWidgetPayload getPayload(WidgetType type,
                                      List<WidgetDataConfig> configs,
                                      int journalId,
                                      List<FlatRecord> recordData);
}
