package com.andhug.relay.invite.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andhug.relay.invite.application.mapper.InviteMapper;
import com.andhug.relay.invite.domain.model.Invite;
import com.andhug.relay.invite.domain.model.InviteCode;
import com.andhug.relay.invite.domain.model.InviteId;
import com.andhug.relay.invite.domain.repository.InviteRepository;
import com.andhug.relay.invite.infrastructure.web.dto.InviteDto;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.domain.repository.ProfileRepository;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class InviteQueryService {
    
    private final InviteRepository inviteRepository;

    private final WorkspaceRepository workspaceRepository;

    private final ProfileRepository profileRepository;

    private final InviteMapper inviteMapper;

    @Transactional(readOnly = true)
    public InviteDto getInvite(InviteId inviteId) {
        return resolveInviteDto(inviteRepository.findById(inviteId));
    }

    @Transactional(readOnly = true)
    public InviteDto getInvite(InviteCode code) {
        return resolveInviteDto(inviteRepository.findByCode(code));
    }

    private InviteDto resolveInviteDto(Invite invite) {
        Workspace workspace = workspaceRepository.findById(invite.getWorkspaceId());
        Profile sender = profileRepository.findById(invite.getSenderId());

        return inviteMapper.toDto(invite, workspace, sender);

    }
}
