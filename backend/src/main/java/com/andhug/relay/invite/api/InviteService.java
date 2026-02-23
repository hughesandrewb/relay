package com.andhug.relay.invite.api;

import com.andhug.relay.invite.internal.InviteEntity;
import com.andhug.relay.invite.internal.InviteMapper;
import com.andhug.relay.invite.internal.InviteRepository;
import com.andhug.relay.profile.internal.ProfileEntity;
import com.andhug.relay.profile.internal.ProfileRepository;
import com.andhug.relay.utils.RandomUtils;
import com.andhug.relay.workspace.api.WorkspaceService;
import com.andhug.relay.workspace.internal.WorkspaceEntity;
import com.andhug.relay.workspace.internal.WorkspaceRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;

    private final ProfileRepository profileRepository;

    private final WorkspaceRepository workspaceRepository;

    private final InviteMapper inviteMapper;

    private final WorkspaceService workspaceService;

    @Transactional
    public Invite getInvite(UUID workspaceId, UUID profileId) {

        Optional<InviteEntity> inviteEntity = inviteRepository.findByWorkspaceIdAndSenderId(workspaceId,
                profileId);

        if (inviteEntity.isPresent()) {
            Invite invite = inviteMapper.toDomain(inviteEntity.get());

            if (!invite.isExpired()) {
                return invite;
            }

            inviteRepository.delete(inviteEntity.get());
        }

        try {
            return this.createInvite(workspaceId, profileId);
        } catch (DataIntegrityViolationException e) {
            return inviteMapper.toDomain(
                    inviteRepository.findByWorkspaceIdAndSenderId(workspaceId, profileId)
                            .orElseThrow(() -> new IllegalStateException("Invite already exists")));
        }
    }

    @Transactional(readOnly = true)
    public Invite getInvite(String code) {

        InviteEntity inviteEntity = inviteRepository.findByCode(code)
                .orElseThrow(() -> new NoResultException("Invite code not found"));

        Invite invite = inviteMapper.toDomain(inviteEntity);

        if (invite.isExpired()) {
            throw new RuntimeException("Invite expired");
        }

        return invite;
    }

    @Transactional
    public Invite createInvite(UUID workspaceId, UUID senderId) {

        // TODO: check profile permissions

        ProfileEntity senderEntity = profileRepository.getReferenceById(senderId);
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new NoResultException("Workspace not found"));

        var inviteEntity = InviteEntity.builder()
                .code(this.generateCode())
                .workspace(workspaceEntity)
                .sender(senderEntity)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        InviteEntity savedInvite = inviteRepository.save(inviteEntity);

        return inviteMapper.toDomain(savedInvite);
    }

    @Transactional
    public void acceptInvite(String code, UUID profileId) {

        Invite invite = this.getInvite(code);

        workspaceService.joinWorkspace(profileId, invite.getWorkspaceId());
    }

    private String generateCode() {

        String code;

        do {
            code = RandomUtils.generateRandomCode(8);
        } while (inviteRepository.existsByCode(code));

        return code;
    }
}
