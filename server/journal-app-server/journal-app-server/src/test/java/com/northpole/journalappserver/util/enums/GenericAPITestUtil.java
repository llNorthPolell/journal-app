package com.northpole.journalappserver.util.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class GenericAPITestUtil {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String endpoint;

    public GenericAPITestUtil(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            String endpoint
    ){
        this.endpoint = endpoint;
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }


    public MvcResult doGenericPostValidationTest(Object originalMockObject, String keyValueString, String replace, APIResult result) throws Exception{
        String testJson = objectMapper.writeValueAsString(originalMockObject)
                .replace(keyValueString,replace);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testJson)
                                .with(jwt())
                )
                .andExpect(result.value()).andReturn();
    }

}
