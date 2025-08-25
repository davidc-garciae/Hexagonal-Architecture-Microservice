package com.pragma.powerup.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.ObjectRequestDto;
import com.pragma.powerup.application.dto.response.ObjectResponseDto;
import com.pragma.powerup.application.handler.IObjectHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ObjectRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ObjectRestControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IObjectHandler objectHandler;

    @TestConfiguration
    static class TestConfig {
        @Bean
        IObjectHandler objectHandler() {
            return Mockito.mock(IObjectHandler.class);
        }
    }

    @Test
    @DisplayName("GET /api/v1/object/ retorna 200 con lista")
    void getAllObjects_ok() throws Exception {
        ObjectResponseDto r1 = new ObjectResponseDto();
        r1.setId(1L);
        r1.setName("one");
        Mockito.when(objectHandler.getAllObjects()).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/v1/object/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /api/v1/object/ retorna 201")
    void saveObject_created() throws Exception {
        ObjectRequestDto req = new ObjectRequestDto();
        req.setName("new");

        mockMvc.perform(post("/api/v1/object/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}
