package com.andhug.relay.room.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RoomProfileKey implements Serializable {

    @Column(name = "room_id")
    private UUID roomId;

    @Column(name = "profile_id")
    private UUID profileId;
}
