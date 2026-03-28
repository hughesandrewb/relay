package com.andhug.relay.invite.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.andhug.relay.invite.application.mapper.InviteMapper;
import com.andhug.relay.invite.domain.model.Invite;
import com.andhug.relay.invite.domain.model.InviteCode;
import com.andhug.relay.invite.domain.model.InviteId;
import com.andhug.relay.invite.domain.repository.InviteRepository;
import com.andhug.relay.shared.domain.exception.NotFoundException;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class InviteRepositoryImpl implements InviteRepository {

    private final InviteJpaRepository inviteJpaRepository;

    private final InviteMapper inviteMapper;

    @Override
	public Invite findById(InviteId inviteId) {
        InviteEntity inviteEntity = inviteJpaRepository.findById(inviteId.value())
            .orElseThrow(() -> new NotFoundException(String.format("Could not find Invite with id: %s", inviteId.toString())));

        return inviteMapper.toDomain(inviteEntity);
	}

    @Override
    public Invite findByCode(InviteCode code) {
        InviteEntity inviteEntity = inviteJpaRepository.findByCode(code.value())
            .orElseThrow(() -> new NotFoundException(String.format("Could not find Invite with code: %s", code.toString())));

        return inviteMapper.toDomain(inviteEntity);
    }

	@Override
	public Invite findByWorkspaceIdAndSenderId(WorkspaceId workspaceId, ProfileId senderId) {
        InviteEntity inviteEntity = inviteJpaRepository.findByWorkspaceIdAndSenderId(workspaceId.value(), senderId.value())
            .orElseThrow(() -> new NotFoundException(String.format("Could not find Invite with WorkspaceId: %s, SenderId: %s", workspaceId, senderId)));

        return inviteMapper.toDomain(inviteEntity);

	}

	@Override
	public void save(Invite invite) {
        InviteEntity inviteEntity = inviteMapper.toEntity(invite);

        inviteJpaRepository.save(inviteEntity);
	}
}
