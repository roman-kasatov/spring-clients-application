package com.unibell.testtask.repository;

import com.unibell.testtask.model.Email;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface EmailRepository extends CrudRepository<Email, Long> {
    @Query("SELECT \"value\" FROM email WHERE CLIENT_ID = :clientId")
    Collection<String> findAllValuesByClientId(Long clientId);

    Optional<Email> findByClientIdAndValue(Long clientId, String value);
}
