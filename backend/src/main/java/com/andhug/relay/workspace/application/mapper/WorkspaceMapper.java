package com.andhug.relay.workspace.application.mapper;

import com.andhug.relay.shared.application.mapper.ValueObjectMapper;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.infrastructure.persistence.WorkspaceEntity;
import com.andhug.relay.workspace.infrastructure.web.dto.WorkspaceDto;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ValueObjectMapper.class })
public interface WorkspaceMapper {

    Workspace toDomain(WorkspaceDto dto);

    Workspace toDomain(WorkspaceEntity dto);

    WorkspaceDto toDto(Workspace domain);

    WorkspaceEntity toEntity(Workspace domain);
}
