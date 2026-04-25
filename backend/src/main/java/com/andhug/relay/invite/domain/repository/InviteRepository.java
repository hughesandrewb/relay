package com.andhug.relay.invite.domain.repository;

import com.andhug.relay.invite.domain.model.Invite;
import com.andhug.relay.invite.domain.model.InviteCode;
import com.andhug.relay.shared.domain.model.InviteId;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;

public interface InviteRepository {
  Invite findById(InviteId inviteId);

  Invite findByWorkspaceIdAndSenderId(WorkspaceId workspaceId, ProfileId senderId);

  Invite findByCode(InviteCode code);

  void save(Invite invite);
}
