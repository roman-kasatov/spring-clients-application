package com.unibell.testtask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ContactsTests {
    @Autowired
    MockMvc mvc;

    @Test
    void shouldReturnEmailsAndPhoneNumbersById() throws Exception {
        mvc.perform(get("/clients/1/email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("email1@test.com"))
                .andExpect(jsonPath("$[1]").value("email2@test.com"));

        mvc.perform(get("/clients/1/phone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").value("+7(999)111-11-11"));
    }

    @Test
    void shouldRejectRequestOfIncorrectContactType() throws Exception {
        mvc.perform(get("/clients/1/address"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllContacts() throws Exception {
        mvc.perform(get("/clients/1/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email.length()").value(2))
                .andExpect(jsonPath("$.email[0]").value("email1@test.com"))
                .andExpect(jsonPath("$.email[1]").value("email2@test.com"))
                .andExpect(jsonPath("$.phone.length()").value(1))
                .andExpect(jsonPath("$.phone[0]").value("+7(999)111-11-11"));
    }

    @Test
    void shouldRejectRequestOnClientThatDoesNotExist() throws Exception {
        mvc.perform(get("/clients/3/email"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/clients/3/phone"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/clients/3/contact"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void shouldAddNewContact() throws Exception {
        mvc.perform(post("/clients/2")
                .content("{\"email\": \"email3@test.com\"}")
                .contentType("application/json"))
                .andExpect(status().isOk());
        mvc.perform(post("/clients/2")
                .content("{\"phone\": \"+7(999)333-33-33\"}")
                .contentType("application/json"))
                .andExpect(status().isOk());

        mvc.perform(get("/clients/2/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email.length()").value(1))
                .andExpect(jsonPath("$.email[0]").value("email3@test.com"))
                .andExpect(jsonPath("$.phone.length()").value(1))
                .andExpect(jsonPath("$.phone[0]").value("+7(999)333-33-33"));
    }

    @Test
    @DirtiesContext
    void shouldNotAddSameContactTwice() throws Exception {
        mvc.perform(post("/clients/2")
                        .content("{\"email\": \"email3@test.com\"}")
                        .contentType("application/json"))
                .andExpect(status().isOk());
        mvc.perform(post("/clients/2")
                        .content("{\"email\": \"email3@test.com\"}")
                        .contentType("application/json"))
                .andExpect(status().isOk());
        mvc.perform(get("/clients/2/contact"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email.length()").value(1))
                .andExpect(jsonPath("$.email[0]").value("email3@test.com"));
    }

    @Test
    @DirtiesContext
    void shouldRejectAddContactOnCorruptedJson() throws Exception {
        mvc.perform(post("/clients/2")
                        .content("{\"address\": \"New York City\"}")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/clients/2")
                        .content("{\"addres York City\"}")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

}
