package com.andhug.relay.workspace.domain.model;

import com.andhug.relay.shared.domain.model.AggregateRoot;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspace.domain.event.WorkspaceCreatedEvent;
import com.andhug.relay.workspace.domain.exception.InvalidWorkspaceNameException;
import com.andhug.relay.workspace.domain.exception.InvalidWorkspaceOwnerException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace extends AggregateRoot {

  private WorkspaceId id;

  private String name;

  private ProfileId ownerId;

  private LocalDateTime createdAt;

  public static Workspace create(String name, ProfileId ownerId) {
    Workspace created =
        Workspace.builder().id(WorkspaceId.generate()).name(name).ownerId(ownerId).build();

    created.registerEvent(new WorkspaceCreatedEvent(created.id));

    return created;
  }

  public void rename(String newName) {
    if (newName == null || newName.isBlank()) {
      throw new InvalidWorkspaceNameException();
    }
    this.name = newName;
  }

  public void changeOwner(ProfileId newOwnerId) {
    if (newOwnerId == null) {
      throw new InvalidWorkspaceOwnerException();
    }
    this.ownerId = newOwnerId;
  }

  public boolean isOwnedBy(ProfileId userId) {
    if (userId == null) {
      throw new InvalidWorkspaceOwnerException();
    }
    return this.ownerId.equals(userId);
  }
}
