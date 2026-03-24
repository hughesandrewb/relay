package com.andhug.relay.workspace.domain.model;

import java.util.UUID;

import com.andhug.relay.workspace.domain.exception.InvalidWorkspaceNameException;
import com.andhug.relay.workspace.domain.exception.InvalidWorkspaceOwnerException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace {

    private UUID id;

    private String name;

    private UUID ownerId;

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new InvalidWorkspaceNameException();
        }
        this.name = newName;
    }

    public void changeOwner(UUID newOwnerId) {
        if (newOwnerId == null) {
            throw new InvalidWorkspaceOwnerException();
        }
        this.ownerId = newOwnerId;
    }

    public boolean isOwnedBy(UUID userId) {
        if (userId == null) {
            throw new InvalidWorkspaceOwnerException();
        }
        return this.ownerId.equals(userId);
    }
}
