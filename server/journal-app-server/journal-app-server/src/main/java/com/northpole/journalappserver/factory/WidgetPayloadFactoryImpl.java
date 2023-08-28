package com.northpole.journalappserver.factory;

import com.northpole.journalappserver.entity.DashboardWidgetPayload;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.entity.WidgetDataConfig;
import com.northpole.journalappserver.entity.widgetpayload.*;
import com.northpole.journalappserver.service.JournalEntryService;

import java.util.*;
import java.util.stream.Collectors;

public class WidgetPayloadFactory {
    private int journal;
    private List<FlatRecord> dataStore;
    private JournalEntryService journalEntryService;

    public enum WidgetType{
        NOT_EXISTS(""),
        LAST_ENTRY("last-entry"),
        LINE_GRAPH("line-graph");

        public final String value;

        private WidgetType(String value){
            this.value=value;
        }

        public static WidgetType getWidgetType(String input){
            for(WidgetType e : values())
                if (e.value.equals(input))
                    return e;

            return null;
        }
    }

    public WidgetPayloadFactory(
            int journal,
            List<FlatRecord> dataStore,
            JournalEntryService journalEntryService
    ){
        this.journal=journal;
        this.dataStore=dataStore;
        this.journalEntryService=journalEntryService;
    }

    public DashboardWidgetPayload getPayload(WidgetType type, List<WidgetDataConfig> configs){
        switch (type){
            case LAST_ENTRY:
                return createTextPayload(configs);
            case LINE_GRAPH:
                return createChartPayload(configs);
            default:
                return null;
        }
    }

    private TextPayload createTextPayload(List<WidgetDataConfig> configs){
        JournalEntry lastEntry = this.journalEntryService.getLastEntryInJournal(this.journal);

        if (lastEntry == null)
            return new TextPayload("There are no entries in this journal. Please publish a new journal entry to see contents here.");

        return new TextPayload(lastEntry.getOverview());
    }


    private ChartPayload createChartPayload(List<WidgetDataConfig> configs) {
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
        for (FlatRecord r : dataStore){
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
