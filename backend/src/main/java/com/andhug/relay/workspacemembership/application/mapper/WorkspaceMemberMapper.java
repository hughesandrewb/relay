package com.andhug.relay.workspacemembership.application.mapper;

import org.mapstruct.Mapper;

import com.andhug.relay.shared.application.mapper.ValueObjectMapper;
import com.andhug.relay.workspacemembership.domain.model.WorkspaceMember;
import com.andhug.relay.workspacemembership.infrastructure.persistence.WorkspaceMemberEntity;

@Mapper(componentModel = "spring", uses = { ValueObjectMapper.class })
public interface WorkspaceMemberMapper {
    WorkspaceMemberEntity toEntity(WorkspaceMember domain);
    WorkspaceMember toDomain(WorkspaceMemberEntity entity);
}
