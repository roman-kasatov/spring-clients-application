package com.unibell.testtask;

import com.unibell.testtask.model.Client;
import com.unibell.testtask.model.Email;
import com.unibell.testtask.repository.ClientRepository;
import com.unibell.testtask.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class DatabaseTests {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    EmailRepository emailRepository;

    @Test
    void shouldReturnClient() {
        Client client = clientRepository.findById(1L).orElseThrow();
        assertThat(client).isEqualTo(new Client(1L, "Artur"));
    }

    @Test
    void custom() {
        emailRepository.save(new Email(null, 1L, "abc"));
    }

}
