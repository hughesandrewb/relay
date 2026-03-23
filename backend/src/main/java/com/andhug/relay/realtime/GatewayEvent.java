package com.andhug.relay.realtime;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GatewayEvent {
    MESSAGE_CREATE("MESSAGE_CREATE"),
    WORKSPACE_UPDATE("WORKSPACE_UPDATE"),
    ROOM_UPDATE("ROOM_UPDATE");

    private String eventName;

    public GatewayEvent fromEventName(String eventName) {
        for (GatewayEvent event : GatewayEvent.values()) {
            if (event.eventName.equals(eventName)) {
                return event;
            }
        }
        throw new IllegalArgumentException("Unknown event name: " + eventName);
    }
}
