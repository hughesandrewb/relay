package com.andhug.relay.message.domain.model;

public record MessageId(long value) {

    public MessageId {
        if (value <= 0) {
            throw new IllegalArgumentException("MessageId must be a positive long value");
        }
    }
    
    public static MessageId of(long value) {
        return new MessageId(value);
    }
}
