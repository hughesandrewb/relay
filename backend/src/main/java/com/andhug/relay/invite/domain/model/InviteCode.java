package com.andhug.relay.invite.domain.model;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.utils.RandomUtils;

public record InviteCode(String value) {

    private static final int CODE_LENGTH = 8;

    public InviteCode {
        if (value == null || value.matches(String.format("/[a-zA-Z0-9]{%d}", CODE_LENGTH))) {
            throw new InvalidArgumentException(String.format("InviteCode must be %d uppercase, lowercase, or digit characters, actual: %s", CODE_LENGTH, value));
        }
    }

    public static InviteCode generate() {
        return InviteCode.of(RandomUtils.generateRandomCode(CODE_LENGTH));
    }

    public static InviteCode of(String value) {
        return new InviteCode(value);
    }

    public String toString() {
        return value;
    }
}
