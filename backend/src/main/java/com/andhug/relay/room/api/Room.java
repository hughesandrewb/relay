package com.andhug.relay.room.api;

import com.andhug.relay.profile.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Room {

    private UUID id;

    private String name;

    private UUID workspaceId;

    private RoomType type;

    private List<Profile> participants;
}
