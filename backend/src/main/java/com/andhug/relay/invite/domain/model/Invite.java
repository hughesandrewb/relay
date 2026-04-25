package com.andhug.relay.invite.domain.model;

import com.andhug.relay.invite.domain.exception.InviteExpiredException;
import com.andhug.relay.shared.domain.model.InviteId;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invite {

  private InviteId id;

  private InviteCode code;

  private WorkspaceId workspaceId;

  private ProfileId senderId;

  private LocalDateTime expiresAt;

  private LocalDateTime createdAt;

  public static Invite create(WorkspaceId workspaceId, ProfileId senderId) {
    return Invite.builder()
        .id(InviteId.generate())
        .code(InviteCode.generate())
        .workspaceId(workspaceId)
        .senderId(senderId)
        .expiresAt(LocalDateTime.now().plusDays(7))
        .createdAt(LocalDateTime.now())
        .build();
  }

  public void validate() {
    if (this.isExpired()) {
      throw new InviteExpiredException(this.getId());
    }
  }

  public boolean isExpired() {
    return expiresAt.isBefore(LocalDateTime.now());
  }
}
