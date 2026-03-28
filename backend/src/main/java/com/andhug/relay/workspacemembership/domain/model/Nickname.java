package com.andhug.relay.workspacemembership.domain.model;

import org.apache.commons.lang3.StringUtils;

public record Nickname(String value) {

    public Nickname {
        if (value == null) {
            value = StringUtils.EMPTY;
        }
    }

    public static Nickname of(String value) {
        return new Nickname(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
