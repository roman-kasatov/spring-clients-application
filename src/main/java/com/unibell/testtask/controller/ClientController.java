package com.unibell.testtask.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibell.testtask.model.Client;
import com.unibell.testtask.model.Email;
import com.unibell.testtask.model.PhoneNumber;
import com.unibell.testtask.repository.ClientRepository;
import com.unibell.testtask.repository.EmailRepository;
import com.unibell.testtask.repository.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    PhoneNumberRepository phoneNumberRepository;


    @GetMapping("/{clientId}/email")
    private ResponseEntity<Collection<String>> getEmailAddressesOfClient(@PathVariable Long clientId) {
        if (!clientExists(clientId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(emailRepository.findAllValuesByClientId(clientId));
    }

    @GetMapping("/{clientId}/phone")
    private ResponseEntity<Collection<String>> getPhoneNumbersOfClient(@PathVariable Long clientId) {
        if (!clientExists(clientId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(phoneNumberRepository.findAllValuesByClientId(clientId));
    }

    @GetMapping("/{clientId}/contact")
    private ResponseEntity<String> getAllContactsOfClient(@PathVariable Long clientId)
            throws JsonProcessingException {
        if (!clientExists(clientId)) return ResponseEntity.notFound().build();
        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(Map.of(
                "email", emailRepository.findAllValuesByClientId(clientId),
                "phone", phoneNumberRepository.findAllValuesByClientId(clientId)
        ));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    private ResponseEntity<String> createClient(@RequestBody Client client) {
        // ignore client.id
        Client newClient;
        try {
            newClient = clientRepository.save(new Client(null, client.name()));
        } catch (DbActionExecutionException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.created(URI.create(newClient.id().toString())).build();
    }

    @PostMapping("/{clientId}")
    private ResponseEntity<Void> addContact(@PathVariable Long clientId, @RequestBody String body) {
        JsonNode jsonNode;
        try {
            jsonNode = new ObjectMapper().readTree(body);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
        if (!clientExists(clientId)) return ResponseEntity.notFound().build();
        JsonNode field;
        if ((field = jsonNode.get("email")) != null && field.isTextual()) {
            Email newValue = new Email(null, clientId, field.asText());
            if (emailRepository.findByClientIdAndValue(newValue.clientId(), newValue.value()).isEmpty()) {
                emailRepository.save(newValue);
            }
            return ResponseEntity.ok().build();
        }
        if ((field = jsonNode.get("phone")) != null && field.isTextual()) {
            PhoneNumber newValue = new PhoneNumber(null, clientId, field.asText());
            if (phoneNumberRepository.findByClientIdAndValue(newValue.clientId(), newValue.value()).isEmpty()) {
                phoneNumberRepository.save(newValue);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{clientId}")
    private ResponseEntity<Client> getClientById(@PathVariable Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client.get());
    }

    @GetMapping
    private ResponseEntity<Iterable<Client>> getAll() {
        return ResponseEntity.ok(clientRepository.findAll());
    }

    boolean clientExists(Long id) {
        return clientRepository.findById(id).isPresent();
    }
}
