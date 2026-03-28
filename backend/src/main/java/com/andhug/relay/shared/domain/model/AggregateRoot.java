package com.andhug.relay.shared.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.andhug.relay.shared.domain.event.DomainEvent;

public abstract class AggregateRoot {
    
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }
}
