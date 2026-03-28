package com.andhug.relay.room.domain.model;

import com.andhug.relay.room.domain.event.RoomUpdatedEvent;
import com.andhug.relay.room.domain.exception.InvalidRoomNameException;
import com.andhug.relay.shared.domain.model.AggregateRoot;
import com.andhug.relay.shared.domain.model.RoomId;
import com.andhug.relay.shared.domain.model.WorkspaceId;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Room extends AggregateRoot {

    private RoomId id;

    private String name;

    private WorkspaceId workspaceId;

    private RoomType type;

    public static Room create(String name, WorkspaceId workspaceId, RoomType type) {
        return Room.builder()
            .id(RoomId.generate())
            .name(name)
            .workspaceId(workspaceId)
            .type(type)
            .build();
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new InvalidRoomNameException();
        }

        registerEvent(new RoomUpdatedEvent(id));

        this.name = newName;
    }
}
