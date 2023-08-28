package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class DashboardWidgetControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public DashboardWidgetControllerTest(MockMvc mockMvc, ObjectMapper objectMapper){
        this.mockMvc=mockMvc;
        this.objectMapper=objectMapper;
    }
}
