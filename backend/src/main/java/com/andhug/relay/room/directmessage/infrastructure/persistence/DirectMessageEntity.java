package com.andhug.relay.room.directmessage.infrastructure.persistence;

import com.andhug.relay.room.infrastructure.persistence.AbstractRoomEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("DIRECT_MESSAGE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DirectMessageEntity extends AbstractRoomEntity {

  @ElementCollection
  @CollectionTable(name = "room_participants")
  private Set<UUID> participants;
}
