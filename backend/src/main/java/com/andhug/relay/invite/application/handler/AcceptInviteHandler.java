package com.andhug.relay.invite.application.handler;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.andhug.relay.invite.application.command.AcceptInviteCommand;
import com.andhug.relay.invite.domain.model.Invite;
import com.andhug.relay.invite.domain.repository.InviteRepository;
import com.andhug.relay.workspacemembership.domain.model.WorkspaceMember;
import com.andhug.relay.workspacemembership.domain.repository.WorkspaceMemberRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class AcceptInviteHandler {
    
    private final InviteRepository inviteRepository;

    private final WorkspaceMemberRepository workspaceMemberRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void handle(AcceptInviteCommand command) {

        Invite invite = inviteRepository.findByCode(command.code());
        invite.validate();

        var workspaceMember = WorkspaceMember.create(invite.getWorkspaceId(), command.profileId());
        workspaceMemberRepository.save(workspaceMember);
        workspaceMember
            .pullDomainEvents()
            .forEach(eventPublisher::publishEvent);
    }
}
