package com.andhug.relay.room;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Room {

    private UUID id;

    private String name;

    private UUID workspaceId;
}
