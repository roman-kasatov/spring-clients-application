package com.unibell.testtask.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public record Email(@Id Long id, Long clientId, @Column("value") String value) {
}
