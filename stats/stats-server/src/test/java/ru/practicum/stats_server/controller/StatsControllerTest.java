package ru.practicum.stats_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats_dto.CreateHitDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan({"ru.practicum.stats_server"})
@WebMvcTest(StatsController.class)
@AutoConfigureDataJpa
@ActiveProfiles("test")
class StatsControllerTest {
    private CreateHitDto createHit;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        String app = "ewm-main-service";
        String ip = "192.168.1.1";
        String uri = "/events/1";
        String timestamp = "2022-05-05 00:00:00";

        createHit = new CreateHitDto();
        createHit.setApp(app);
        createHit.setIp(ip);
        createHit.setUri(uri);
        createHit.setTimestamp(timestamp);
    }

    @DisplayName("Получение статистики для uri: /events/112")
    @Test
    void getStatsForAloneUri() throws Exception {
        createHit.setUri("/events/112");
        createHit.setIp("192.168.255.1");
        mockMvc.perform(post("/hit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createHit)));
        createHit.setIp("192.168.255.2");
        mockMvc.perform(post("/hit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createHit)));

        mockMvc.perform(get("/stats")
                        .param("start", "2020-05-05 00:00:00")
                        .param("end", "2030-05-05 23:59:59")
                        .param("uris", "/events/112"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].hits").value(2));
    }

    @DisplayName("Получение статистики для uri: /events/1")
    @Test
    void getStatsForAloneIp() throws Exception {
        createHit.setIp("192.168.244.1");
        mockMvc.perform(post("/hit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createHit)));
        mockMvc.perform(post("/hit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createHit)));

        mockMvc.perform(get("/stats")
                        .param("start", "2020-05-05 00:00:00")
                        .param("end", "2030-05-05 23:59:59")
                        .param("uris", "/events/1")
                        .param("unique", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].hits").value(1));
    }

    @DisplayName("Получение статистики для uri: /events/911 и /events/114")
    @Test
    void getStatsForTwoUrl() throws Exception {
        createHit.setUri("/events/911");
        createHit.setIp("192.168.224.1");
        mockMvc.perform(post("/hit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createHit)));
        createHit.setUri("/events/114");
        createHit.setIp("192.168.224.2");
        mockMvc.perform(post("/hit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createHit)));

        mockMvc.perform(get("/stats")
                        .param("start", "2020-05-05 00:00:00")
                        .param("end", "2030-05-05 23:59:59")
                        .param("uris", "/events/911", "/events/114"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].hits").value(1))
                .andExpect(jsonPath("$[1].hits").value(1));
    }

    @DisplayName("Добавление данных на сервер статистики")
    @Test
    void createHit() throws Exception {
        mockMvc.perform(post("/hit").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createHit)))
                .andExpect(status().isCreated());
    }
}