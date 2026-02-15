package com.andhug.relay.workspace.api;

import com.andhug.relay.invite.api.Invite;
import com.andhug.relay.invite.api.InviteService;
import com.andhug.relay.invite.api.dto.response.GetInviteResponse;
import com.andhug.relay.profile.ProfileDto;
import com.andhug.relay.room.Room;
import com.andhug.relay.room.RoomDto;
import com.andhug.relay.room.RoomService;
import com.andhug.relay.workspace.api.dto.WorkspaceDto;
import com.andhug.relay.workspace.api.dto.request.CreateWorkspaceRequest;
import com.andhug.relay.workspace.internal.WorkspaceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Workspace Management", description = "APIs for managing workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    private final RoomService roomService;

    private final WorkspaceMapper workspaceMapper;

    private final InviteService inviteService;

    @GetMapping(value = "/{workspaceId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get workspace by ID", description = "Retrieves a specific workspace by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace found"),
            @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<WorkspaceDto> getWorkspace(
            @Parameter(description = "Workspace ID", required = true)
            @PathVariable UUID workspaceId) {

        Workspace workspace = workspaceService.findById(workspaceId);

        return ResponseEntity.ok(workspaceMapper.toDto(workspace));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create workspace", description = "Create a workspace with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace created successfully")
    })
    public ResponseEntity<WorkspaceDto> createWorkspace(
            @RequestBody CreateWorkspaceRequest request) {

        var partial = Workspace.builder()
                .name(request.name())
                .build();

        Workspace workspace = workspaceService.createWorkspace(partial);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workspaceMapper.toDto(workspace));
    }

    @GetMapping(value = "/{workspaceId}/rooms", produces =  MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get workspace rooms", description = "Returns list of room objects associated with provided workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of room objects"),
            @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<List<RoomDto>> getRooms(
            @Parameter(description = "Workspace ID", required = true)
            @PathVariable("workspaceId") UUID workspaceId) {

        List<Room> rooms = roomService.getRoomsByWorkspaceId(workspaceId);

        return ResponseEntity.ok(rooms.stream().map(room -> {
            return RoomDto.builder()
                    .id(room.getId())
                    .name(room.getName())
                    .workspaceId(room.getWorkspaceId())
                    .build();
        }).toList());
    }

    @PostMapping(value = "/{workspaceId}/rooms", produces =  MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create workspace room", description = "Creates a room object to be associated with provided workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room created successfully"),
            @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<RoomDto> createRoom(
        @Parameter(description = "Workspace ID", required = true)
        @PathVariable("workspaceId") UUID workspaceId) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GetMapping(value = "/{workspaceId}/members", produces =   MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get members", description = "Returns a list of profiles that are members of provided workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of Profile objects"),
            @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<List<ProfileDto>> getMembers(
            @Parameter(description = "Workspace ID", required = true)
            @PathVariable("workspaceId") UUID workspaceId,

            @Parameter(description = "Maximum number of messages to return")
            @RequestParam(required = false, defaultValue = "50") int limit,

            @Parameter(description = "Only returns messages after the message with this ID")
            @RequestParam(required = false) Long after) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GetMapping(value = "/{workspaceId}/invite", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get workspace invite", description = "Returns an invite for the workspace to send to another user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workspace invite"),
            @ApiResponse(responseCode = "404", description = "Workspace not found")
    })
    public ResponseEntity<GetInviteResponse> getInvite(
            @Parameter(description = "Workspace ID", required = true)
            @PathVariable("workspaceId") UUID workspaceId
    ) {

        Invite invite = inviteService.getInvite(workspaceId);

        return ResponseEntity.ok(GetInviteResponse.builder()
                .code(invite.getCode())
                .expiresAt(invite.getExpiresAt())
                .createdAt(invite.getCreatedAt())
                .build());
    }
}
