package com.andhug.relay.invite.application.mapper;

import com.andhug.relay.invite.domain.model.Invite;
import com.andhug.relay.invite.infrastructure.persistence.InviteEntity;
import com.andhug.relay.invite.infrastructure.web.dto.InviteDto;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.shared.application.mapper.ValueObjectMapper;
import com.andhug.relay.workspace.domain.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {ValueObjectMapper.class})
public interface InviteMapper {
  Invite toDomain(InviteEntity entity);

  InviteEntity toEntity(Invite domain);

  @Mapping(target = "createdAt", source = "invite.createdAt")
  InviteDto toDto(Invite invite, Workspace workspace, Profile sender);
}
