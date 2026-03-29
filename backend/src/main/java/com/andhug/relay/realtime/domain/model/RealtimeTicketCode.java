package com.andhug.relay.realtime.domain.model;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.utils.RandomUtils;

public record RealtimeTicketCode(String value) {

    public static final int REALTIME_TICKET_CODE_LENGTH = 16;

    public RealtimeTicketCode {
        if (value == null || value.length() != REALTIME_TICKET_CODE_LENGTH) {
            throw new InvalidArgumentException("Invalid ticket code");
        }
    }
    
    public static RealtimeTicketCode generate() {
        return new RealtimeTicketCode(RandomUtils.generateRandomCode(REALTIME_TICKET_CODE_LENGTH));
    }

    public static RealtimeTicketCode of(String value) {
        return new RealtimeTicketCode(value);
    }

    public String getKey() {
        return "ws:ticket:" + value;
    }

    @Override
    public String toString() {
        return value;
    }
}
