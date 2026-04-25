package com.andhug.relay.invite.domain.exception;

import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.InviteId;

public class InviteExpiredException extends DomainException {

  public InviteExpiredException() {
    super("Invite is expired");
  }

  public InviteExpiredException(InviteId inviteId) {
    super(String.format("Invite is expired, id: %s", inviteId.toString()));
  }
}
