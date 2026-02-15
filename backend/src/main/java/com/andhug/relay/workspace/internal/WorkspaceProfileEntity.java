package com.andhug.relay.workspace.internal;

import com.andhug.relay.profile.internal.ProfileEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "workspace_profile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceProfileEntity {

    @EmbeddedId
    private WorkspaceProfileKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("workspaceId")
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("profileId")
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile;

    @Column(length = 32)
    private String nickname;

    @CreatedDate
    private LocalDateTime joinedAt;
}
