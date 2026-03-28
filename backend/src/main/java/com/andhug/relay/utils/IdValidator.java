package com.andhug.relay.utils;

import java.util.UUID;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;

public class IdValidator {
    public static UUID validate(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidArgumentException(String.format("%s cannot be null or blank", fieldName));
        }

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentException(String.format("%s must be a valid UUID: %s", fieldName, value));
        }
    }
}
