package com.andhug.relay.workspacemembership.application.mapper;

import com.andhug.relay.shared.application.mapper.ValueObjectMapper;
import com.andhug.relay.workspacemembership.domain.model.WorkspaceMember;
import com.andhug.relay.workspacemembership.infrastructure.persistence.WorkspaceMemberEntity;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {ValueObjectMapper.class})
public interface WorkspaceMemberMapper {
  WorkspaceMemberEntity toEntity(WorkspaceMember domain);

  WorkspaceMember toDomain(WorkspaceMemberEntity entity);
}
