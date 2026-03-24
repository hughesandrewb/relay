package com.andhug.relay.workspace.application.mapper;

import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.infrastructure.persistence.WorkspaceEntity;
import com.andhug.relay.workspace.infrastructure.web.dto.WorkspaceDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {

    Workspace toDomain(WorkspaceDto dto);

    Workspace toDomain(WorkspaceEntity dto);

    WorkspaceDto toDto(Workspace domain);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    WorkspaceEntity toEntity(Workspace domain);
}
