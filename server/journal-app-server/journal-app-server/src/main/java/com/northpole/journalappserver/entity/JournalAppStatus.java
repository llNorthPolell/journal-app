package com.northpole.journalappserver.entity;

public enum JournalAppStatus {
    NA("NA"),
    IN_PROGRESS("IN PROGRESS"),
    FAIL("FAILED"),
    COMPLETE("COMPLETE");

    private final String value;

    JournalAppStatus(String value){
        this.value=value;
    }

    public String value(){
        return this.value;
    }

    public static JournalAppStatus getValue(String input){
        for(JournalAppStatus e : values())
            if (e.value.equals(input))
                return e;

        return NA;
    }
}
