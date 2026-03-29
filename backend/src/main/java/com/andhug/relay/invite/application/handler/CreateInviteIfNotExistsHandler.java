package com.andhug.relay.invite.application.handler;

import com.andhug.relay.invite.application.command.CreateInviteIfNotExistsCommand;
import com.andhug.relay.invite.domain.model.Invite;
import com.andhug.relay.invite.domain.model.InviteId;
import com.andhug.relay.invite.domain.repository.InviteRepository;
import com.andhug.relay.shared.domain.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CreateInviteIfNotExistsHandler {

  private final InviteRepository inviteRepository;

  @Transactional
  public InviteId handle(CreateInviteIfNotExistsCommand command) {
    try {
      return inviteRepository
          .findByWorkspaceIdAndSenderId(command.workspaceId(), command.senderId())
          .getId();
    } catch (NotFoundException e) {
      Invite toCreate = Invite.create(command.workspaceId(), command.senderId());
      inviteRepository.save(toCreate);

      return toCreate.getId();
    }
  }
}
