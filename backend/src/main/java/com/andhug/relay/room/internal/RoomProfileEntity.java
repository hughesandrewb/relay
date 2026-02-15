package com.andhug.relay.room.internal;

import com.andhug.relay.profile.internal.ProfileEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room_profile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomProfileEntity {

    @EmbeddedId
    private RoomProfileKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("profileId")
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile;
}
