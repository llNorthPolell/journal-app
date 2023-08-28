package com.northpole.journalappserver.util.enums;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public enum APIResult {
    FAIL(status().isBadRequest()), PASS(status().isOk());

    private ResultMatcher result;

    APIResult(ResultMatcher result){
        this.result=result;
    }

    public ResultMatcher value(){
        return this.result;
    }

}