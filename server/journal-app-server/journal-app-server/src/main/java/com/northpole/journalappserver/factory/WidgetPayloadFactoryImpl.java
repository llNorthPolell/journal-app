package com.northpole.journalappserver.factory;

import com.northpole.journalappserver.entity.DashboardWidgetPayload;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.entity.WidgetDataConfig;
import com.northpole.journalappserver.entity.widgetpayload.*;
import com.northpole.journalappserver.service.JournalEntryService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WidgetPayloadFactoryImpl implements WidgetPayloadFactory {
    private JournalEntryService journalEntryService;

    public WidgetPayloadFactoryImpl(
            JournalEntryService journalEntryService
    ){
        this.journalEntryService=journalEntryService;
    }

    public DashboardWidgetPayload getPayload(
            WidgetType type,
            List<WidgetDataConfig> configs,
            int journalId,
            List<FlatRecord> recordData
    ){
        switch (type){
            case LAST_ENTRY:
                return createTextPayload(configs, journalId);
            case LINE_GRAPH:
                return createChartPayload(configs,recordData);
            default:
                return null;
        }
    }

    // configs is a placeholder for now; will modify this when time comes where new text-based widget is required
    private TextPayload createTextPayload(List<WidgetDataConfig> configs, int journalId){
        JournalEntry lastEntry = this.journalEntryService.getLastEntryInJournal(journalId);

        if (lastEntry == null)
            return new TextPayload("There are no entries in this journal. Please publish a new journal entry to see contents here.");

        return new TextPayload(lastEntry.getOverview());
    }


    private ChartPayload createChartPayload(
            List<WidgetDataConfig> configs,
            List<FlatRecord> recordData
    ) {
        String xRule = null;
        List<String> yRules = new ArrayList<>();

        List<String> labels = new ArrayList<>();
        List<Dataset> datasets = new ArrayList<>();

        Map<String, String[]> datasetBuffer = new LinkedHashMap<>();

        // Extract Rules
        for (WidgetDataConfig config : configs) {
            switch(config.getType()){
                case "x":
                    xRule = config.getRule();
                    break;
                case "y":
                    String color =  (config.getColor()==null)? "#000000" : config.getColor();
                    yRules.add(config.getRule());
                    datasets.add(Dataset.builder()
                            .label(config.getLabel())
                            .data(new ArrayList<>())
                            .borderColor(color)
                            .backgroundColor(color)
                            .build()
                    );
                    break;
                default:
                    break;
            }
        }

        // Check if xRule and at least one yRule are populated
        if (xRule == null || yRules.isEmpty()) {
            System.err.println("Failed to generate payload due to missing dimensions...");
            return null;
        }

        // Filter and Group Datasets
        for (FlatRecord r : recordData){
            String key = r.getDateOfEntry().toString();
            for (int i = 0 ; i < yRules.size(); i ++)
                if (yRules.get(i).equals(r.getRecKey())){
                    if (!datasetBuffer.containsKey(key))
                        datasetBuffer.put(r.getDateOfEntry().toString(),new String[yRules.size()]);
                    datasetBuffer.get(key)[i] = r.getRecValue();
                    break;
                }
        }

        // Output
        for (Map.Entry<String, String[]> pointSet : datasetBuffer.entrySet()){
            labels.add(pointSet.getKey());
            for (int i=0; i < yRules.size(); i++)
                datasets.get(i).getData().add(pointSet.getValue()[i]);
        }


        return ChartPayload.builder()
                .labels(labels)
                .datasets(datasets)
                .build();
    }
}
