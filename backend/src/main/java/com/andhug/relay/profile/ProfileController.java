package com.andhug.relay.profile;

import com.andhug.relay.room.Room;
import com.andhug.relay.room.RoomDto;
import com.andhug.relay.room.RoomService;
import com.andhug.relay.profile.internal.ProfileMapper;
import com.andhug.relay.workspace.api.Workspace;
import com.andhug.relay.workspace.api.WorkspaceService;
import com.andhug.relay.workspace.api.dto.WorkspaceSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Profile Controller", description = "APIs for managing profiles")
public class ProfileController {

    private final WorkspaceService workspaceService;

    private final RoomService roomService;

    private final ProfileMapper profileMapper;

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get current profile's information", description = "Returns the current profile's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile information"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ProfileDto getMe(
            @AuthenticationPrincipal Profile profile
    ) {

        return profileMapper.toDto(profile);
    }

    @GetMapping(value = "/me/workspaces",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get current profile's workspaces", description = "Returns a list of workspaces that the current profile is a member of")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of current profile's workspaces"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<WorkspaceSummaryDto> getWorkspaces(
            @AuthenticationPrincipal Profile profile
    ) {

        List<Workspace> workspaces = workspaceService.findAllByProfileId(profile.getId());

        return workspaces.stream()
                .map(workspace -> WorkspaceSummaryDto.builder()
                            .id(workspace.getId())
                            .name(workspace.getName())
                        .owner(workspace.getOwnerId().equals(profile.getId()))
                            .build())
                .toList();
    }

    @GetMapping(value = "/me/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get current profile's direct messages", description = "Returns a list of direct message objects that the current profile is a participant of")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of current profile's direct messages")
    })
    public List<RoomDto> getDirectMessages(
            @AuthenticationPrincipal Profile profile
    ) {

        List<Room> rooms = roomService.getRoomsByProfileId(profile.getId());

        throw new UnsupportedOperationException("Not supported yet.");
    }

}
