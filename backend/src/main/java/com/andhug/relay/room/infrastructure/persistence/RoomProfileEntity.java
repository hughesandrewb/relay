package com.andhug.relay.room.infrastructure.persistence;

import com.andhug.relay.profile.infrastructure.persistence.ProfileEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room_profile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomProfileEntity {

  @EmbeddedId private RoomProfileKey id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("roomId")
  @JoinColumn(name = "room_id", nullable = false)
  private RoomEntity room;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("profileId")
  @JoinColumn(name = "profile_id", nullable = false)
  private ProfileEntity profile;
}
