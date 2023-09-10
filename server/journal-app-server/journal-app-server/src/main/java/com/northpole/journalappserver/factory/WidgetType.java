package com.northpole.journalappserver.factory;

public enum WidgetType{
    NOT_EXISTS(""),
    LAST_ENTRY("last-entry"),
    LINE_GRAPH("line-graph"),
    BAR_GRAPH("bar-graph");

    public final String value;

    private WidgetType(String value){
        this.value=value;
    }

    public static WidgetType getWidgetType(String input){
        for(WidgetType e : values())
            if (e.value.equals(input))
                return e;

        return NOT_EXISTS;
    }
}