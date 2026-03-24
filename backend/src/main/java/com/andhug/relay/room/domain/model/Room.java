package com.andhug.relay.room.domain.model;

import com.andhug.relay.profile.Profile;
import com.andhug.relay.room.domain.exception.InvalidRoomNameException;
import com.andhug.relay.room.domain.exception.InvalidRoomParticipantException;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Room {

    private UUID id;

    private String name;

    private UUID workspaceId;

    private RoomType type;

    private List<Profile> participants;

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new InvalidRoomNameException();
        }

        this.name = newName;
    }

    public void addParticipant(Profile profile) {
        if (profile == null) {
            throw new InvalidRoomParticipantException();
        }

        if (participants == null) {
            participants = new ArrayList<>();
        }

        if (!participants.contains(profile)) {
            participants.add(profile);
        }
    }
}
