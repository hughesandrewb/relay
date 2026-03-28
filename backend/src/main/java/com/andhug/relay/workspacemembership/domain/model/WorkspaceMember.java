package com.andhug.relay.workspacemembership.domain.model;

import java.time.Instant;

import com.andhug.relay.shared.domain.model.AggregateRoot;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.shared.domain.model.WorkspaceMemberId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WorkspaceMember extends AggregateRoot {
    
    private WorkspaceMemberId id;

    private WorkspaceId workspaceId;

    private ProfileId profileId;

    private Nickname nickname;

    private Instant joinedAt;

    public static WorkspaceMember create(WorkspaceId workspaceId, ProfileId profileId) {
        return WorkspaceMember.create(workspaceId, profileId, Nickname.of(null));
    }

    public static WorkspaceMember create(WorkspaceId workspaceId, ProfileId profileId, Nickname nickname) {
        return WorkspaceMember.builder()
            .id(WorkspaceMemberId.generate())
            .workspaceId(workspaceId)
            .profileId(profileId)
            .nickname(nickname)
            .joinedAt(Instant.now())
            .build();
    }
}
