package com.andhug.relay.workspace.infrastructure.web;

import com.andhug.relay.invite.application.command.CreateInviteIfNotExistsCommand;
import com.andhug.relay.invite.application.handler.CreateInviteIfNotExistsHandler;
import com.andhug.relay.invite.application.service.InviteQueryService;
import com.andhug.relay.invite.domain.model.InviteId;
import com.andhug.relay.invite.infrastructure.web.dto.InviteDto;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;
import com.andhug.relay.room.application.command.CreateRoomCommand;
import com.andhug.relay.room.application.service.RoomCommandService;
import com.andhug.relay.room.application.service.RoomQueryService;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.service.RoomDomainService;
import com.andhug.relay.room.infrastructure.web.dto.RoomDto;
import com.andhug.relay.room.infrastructure.web.dto.request.CreateRoomRequest;
import com.andhug.relay.shared.domain.model.RoomId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspace.application.command.CreateWorkspaceCommand;
import com.andhug.relay.workspace.application.command.UpdateWorkspaceCommand;
import com.andhug.relay.workspace.application.handler.CreateWorkspaceHandler;
import com.andhug.relay.workspace.application.mapper.WorkspaceMapper;
import com.andhug.relay.workspace.application.service.WorkspaceCommandService;
import com.andhug.relay.workspace.application.service.WorkspaceQueryService;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.infrastructure.web.dto.WorkspaceDto;
import com.andhug.relay.workspace.infrastructure.web.dto.request.CreateWorkspaceRequest;
import com.andhug.relay.workspace.infrastructure.web.dto.request.UpdateWorkspaceRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Workspace Management", description = "APIs for managing workspaces")
public class WorkspaceController {

    private final WorkspaceCommandService workspaceApplicationService;

    private final RoomCommandService roomCommandService;

    private final RoomQueryService roomQueryService;

    private final CreateWorkspaceHandler createWorkspaceHandler;

    private final WorkspaceQueryService workspaceQueryService;

    private final RoomDomainService roomService;

    private final WorkspaceMapper workspaceMapper;

    private final InviteQueryService inviteQueryService;

    private final CreateInviteIfNotExistsHandler createInviteIfNotExistsHandler;

    @GetMapping(value = "/{workspace-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get workspace by ID", description = "Retrieves a specific workspace by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Workspace found"),
        @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<WorkspaceDto> getWorkspace(
        @Parameter(in = ParameterIn.PATH, required = true, name = "workspace-id", schema = @Schema(type = "string"))
        @PathVariable("workspace-id") UUID workspaceId
    ) {

        WorkspaceDto workspace = workspaceQueryService
            .getWorkspace(WorkspaceId.of(workspaceId));

        return ResponseEntity
            .ok(workspace);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create workspace", description = "Create a workspace with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Workspace created successfully")
    })
    public ResponseEntity<WorkspaceDto> createWorkspace(
        @AuthenticationPrincipal Profile profile,
        @RequestBody CreateWorkspaceRequest request
    ) {

        var createWorkspaceCommand = CreateWorkspaceCommand.builder()
            .name(request.name())
            .ownerId(profile.getId())
            .build();

        WorkspaceId workspaceId = createWorkspaceHandler.handle(createWorkspaceCommand);

        WorkspaceDto workspace = workspaceQueryService.getWorkspace(workspaceId);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(workspace);
    }

    @PatchMapping(value = "/{workspace-id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update workspace", description = "Update a workspace with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Workspace updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized to update workspace"),
        @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<WorkspaceDto> updateWorkspace(
        @AuthenticationPrincipal Profile profile,
        @Parameter(in = ParameterIn.PATH, required = true, name = "workspace-id", schema = @Schema(type = "string"))
        @PathVariable("workspace-id") UUID workspaceId,
        @RequestBody UpdateWorkspaceRequest request
    ) {

        var command = UpdateWorkspaceCommand.builder()
            .workspaceId(WorkspaceId.of(workspaceId))
            .requesterId(profile.getId())
            .name(Optional.ofNullable(request.name()))
            .ownerId(Optional.empty())
            .build();

        Workspace workspace = workspaceApplicationService
            .updateWorkspace(command);

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(workspaceMapper.toDto(workspace));
    }

    @GetMapping(value = "/{workspace-id}/rooms", produces =  MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get workspace rooms", description = "Returns list of room objects associated with provided workspace")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of room objects"),
        @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<List<RoomDto>> getRooms(
        @Parameter(in = ParameterIn.PATH, required = true, name = "workspace-id", schema = @Schema(type = "string"))
        @PathVariable("workspace-id") UUID workspaceId
    ) {

        List<Room> rooms = roomService.getRoomsByWorkspaceId(workspaceId);

        return ResponseEntity.ok(rooms.stream().map(room -> {
            return RoomDto.builder()
                .id(room.getId().value())
                .name(room.getName())
                .workspaceId(room.getWorkspaceId().value())
                .build();
        }).toList());
    }

    @PostMapping(value = "/{workspace-id}/rooms", produces =  MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create workspace room", description = "Creates a room object to be associated with provided workspace")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Room created successfully"),
        @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<RoomDto> createRoom(
        @Parameter(in = ParameterIn.PATH, required = true, name = "workspace-id", schema = @Schema(type = "string"))
        @PathVariable("workspace-id") UUID workspaceId,
        @RequestBody CreateRoomRequest request
    ) {

        var createRoomCommand = new CreateRoomCommand(WorkspaceId.of(workspaceId), request.name());

        RoomId roomId = roomCommandService.createRoom(createRoomCommand);

        RoomDto room = roomQueryService.getRoom(roomId);

        return ResponseEntity
            .ok(room);
    }

    @GetMapping(value = "/{workspace-id}/members", produces =   MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get members", description = "Returns a list of profiles that are members of provided workspace")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "List of Profile objects"),
        @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<List<ProfileDto>> getMembers(
        @Parameter(in = ParameterIn.PATH, required = true, name = "workspace-id", schema = @Schema(type = "string"))
        @PathVariable("workspace-id") UUID workspaceId,

        @Parameter(in = ParameterIn.QUERY, required = false, name = "limit", schema = @Schema(type = "string"), description = "Maximum number of messages to return")
        @RequestParam(required = false, defaultValue = "50") int limit,

        @Parameter(in = ParameterIn.QUERY, required = false, name = "limit", schema = @Schema(type = "string"), description = "Only returns messages after the message with this ID")
        @RequestParam(required = false) Long after
    ) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GetMapping(value = "/{workspace-id}/invite", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get workspace invite", description = "Returns an invite for the workspace to send to another user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Workspace invite"),
        @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<InviteDto> getInvite(
        @AuthenticationPrincipal Profile profile,
        @Parameter(in = ParameterIn.PATH, required = true, name = "workspace-id", schema = @Schema(type = "string"))
        @PathVariable("workspace-id") UUID workspaceId
    ) {

        var createInviteIfNotExistsCommand = CreateInviteIfNotExistsCommand.builder()
            .workspaceId(WorkspaceId.of(workspaceId))
            .senderId(profile.getId())
            .build();

        InviteId inviteId = createInviteIfNotExistsHandler.handle(createInviteIfNotExistsCommand);

        InviteDto invite = inviteQueryService.getInvite(inviteId);

        return ResponseEntity
            .ok(invite);
    }
}
