package com.unibell.testtask;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ClientTests {

    @Autowired
    MockMvc mvc;


    @Test
    void shouldReturnClients() throws Exception {
        mvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Artur"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Ivan"));
    }

    @Test
    void shouldReturnClientById() throws Exception {
        mvc.perform(get("/clients/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    void shouldReturnNotFoundWhenClientIdNotExists() throws Exception {
        mvc.perform(get("/clients/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void shouldCreateNewClient() throws Exception {
        mvc.perform(post("/clients")
                        .content("""
                {"name": "Alex"}""")
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "3"));

        mvc.perform(get("/clients/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex"));
    }

    @Test
    @DirtiesContext
    void shouldRejectNameLargerThanConstraint() throws Exception {
        mvc.perform(post("/clients")
                .content("{\"name\": \"" + new String(new char[257]) + "\"}")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void shouldRejectCreationOnJsonWithoutFieldName() throws Exception {
        mvc.perform(post("/clients")
                        .content("{\"email\": \"Ivan\"}")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void shouldRejectCreationOnCorruptedJson() throws Exception {
        mvc.perform(post("/clients")
                        .content("{\"email \"Ivan\"}")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
