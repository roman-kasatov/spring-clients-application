package com.unibell.testtask.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

public record Client(@Id Long id, String name) {
}
