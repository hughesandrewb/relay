package com.andhug.relay.invite.internal;

import com.andhug.relay.invite.api.Invite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InviteMapper {

    @Mapping(target = "workspaceId", source = "workspace.id")
    @Mapping(target = "senderId", source = "sender.id")
    Invite toDomain(InviteEntity entity);
}
