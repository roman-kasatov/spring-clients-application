package com.unibell.testtask.repository;

import com.unibell.testtask.model.Email;
import com.unibell.testtask.model.PhoneNumber;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {
    @Query("SELECT \"value\" FROM phone_number WHERE CLIENT_ID = :clientId")
    Collection<String> findAllValuesByClientId(Long clientId);

    Optional<PhoneNumber> findByClientIdAndValue(Long clientId, String value);
}
