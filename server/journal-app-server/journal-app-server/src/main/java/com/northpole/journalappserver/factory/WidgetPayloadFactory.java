package com.northpole.journalappserver.factory;

import com.northpole.journalappserver.entity.DashboardWidgetPayload;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.WidgetDataConfig;

import java.util.List;
import java.util.UUID;

public interface WidgetPayloadFactory {

    DashboardWidgetPayload getPayload(WidgetType type,
                                      List<WidgetDataConfig> configs,
                                      UUID journalRef,
                                      List<FlatRecord> recordData);
}
