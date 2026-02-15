package com.andhug.relay.profile.internal;

import com.andhug.relay.room.internal.RoomProfileEntity;
import com.andhug.relay.workspace.internal.WorkspaceProfileEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String displayName;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RoomProfileEntity> privateRooms = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<WorkspaceProfileEntity> workspaces = new HashSet<>();
}
