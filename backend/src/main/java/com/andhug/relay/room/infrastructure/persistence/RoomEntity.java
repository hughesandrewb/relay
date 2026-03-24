package com.andhug.relay.room.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.andhug.relay.room.domain.model.RoomType;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private RoomType type;

    @Column(length = 100)
    private String name;

    private UUID workspaceId;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RoomProfileEntity> participants = new HashSet<>();
}
