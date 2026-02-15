package com.andhug.relay.workspace.internal;

import com.andhug.relay.workspace.api.Workspace;
import com.andhug.relay.workspace.api.dto.WorkspaceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {

    Workspace toDomain(WorkspaceDto dto);

    Workspace toDomain(WorkspaceEntity dto);

    WorkspaceDto toDto(Workspace domain);

    WorkspaceEntity toEntity(WorkspaceDto dto);

    WorkspaceEntity toEntity(Workspace domain);
}
