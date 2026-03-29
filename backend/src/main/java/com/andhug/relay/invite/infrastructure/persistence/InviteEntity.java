package com.andhug.relay.invite.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "invite",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_invite_workspace_sender",
          columnNames = {"workspace_id", "sender_id"})
    },
    indexes = {@Index(name = "idx_invite_code", columnList = "code", unique = true)})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteEntity {

  @Id private UUID id;

  // @Column(nullable = false, unique = true, length = 8)
  private String code;

  @Column(nullable = false)
  private UUID workspaceId;

  @Column(nullable = false)
  private UUID senderId;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
