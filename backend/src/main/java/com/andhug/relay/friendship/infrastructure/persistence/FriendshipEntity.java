package com.andhug.relay.friendship.infrastructure.persistence;

import com.andhug.relay.friendship.domain.model.FriendshipStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "friendship",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"requester_id", "addressee_id"})})
@Getter
@Setter
@AllArgsConstructor
@Builder
public class FriendshipEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "requester_id", nullable = false, updatable = false)
  private UUID requesterId;

  @Column(name = "addressee_id", nullable = false, updatable = false)
  private UUID addresseeId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @Builder.Default
  private FriendshipStatus status = FriendshipStatus.PENDING;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
