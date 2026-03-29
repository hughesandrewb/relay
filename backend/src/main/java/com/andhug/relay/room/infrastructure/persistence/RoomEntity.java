package com.andhug.relay.room.infrastructure.persistence;

import com.andhug.relay.room.domain.model.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
