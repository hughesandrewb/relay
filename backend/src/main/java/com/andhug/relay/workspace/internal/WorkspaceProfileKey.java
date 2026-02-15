package com.andhug.relay.workspace.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class WorkspaceProfileKey implements Serializable {

    @Column(name = "workspace_id")
    private UUID workspaceId;

    @Column(name = "profile_id")
    private UUID profileId;
}
