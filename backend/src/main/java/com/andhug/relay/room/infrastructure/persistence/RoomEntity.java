package com.andhug.relay.room.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import com.andhug.relay.room.domain.model.RoomType;

@Entity
@DiscriminatorValue("WORKSPACE_ROOM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomEntity extends AbstractRoomEntity {

    private UUID workspaceId;

    @Column(length = 100)
    private String name;

    private RoomType type;
}
