package com.andhug.relay.workspace.domain.event;

import com.andhug.relay.shared.domain.event.DomainEvent;
import com.andhug.relay.shared.domain.model.WorkspaceId;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@org.jmolecules.event.annotation.DomainEvent
@AllArgsConstructor
public class WorkspaceCreatedEvent extends DomainEvent {

    private WorkspaceId workspaceId;

    public static WorkspaceCreatedEvent of(WorkspaceId workspaceId) {
        return new WorkspaceCreatedEvent(workspaceId);
    }
}
