package com.andhug.relay.profile;

import com.andhug.relay.friend.api.FriendService;
import com.andhug.relay.friend.api.dto.FriendSummaryDto;
import com.andhug.relay.friend.api.models.Friendship;
import com.andhug.relay.friend.internal.FriendshipMapper;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Profile Controller", description = "APIs for managing profiles")
public class ProfileController {

    private final WorkspaceService workspaceService;

    private final RoomService roomService;

    private final FriendService friendService;

    private final ProfileMapper profileMapper;

    private final FriendshipMapper friendshipMapper;

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

    @GetMapping(value = "/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FriendSummaryDto> getFriends(
            @PathVariable UUID id
    ) {

        List<Profile> friends = friendService.getFriends(id);

        return friends.stream()
                .map(friendshipMapper::profileToFriendSummaryDto)
                .toList();
    }

    @GetMapping(value = "/me/friends")
    public List<FriendSummaryDto> getFriends(
            @AuthenticationPrincipal Profile profile
    ) {

        return getFriends(profile.getId());
    }

    @PostMapping(value = "/{addresseeId}/friends/request")
    public Friendship requestFriend(
            @AuthenticationPrincipal Profile profile,
            @PathVariable UUID addresseeId
    ) {

        Friendship friendship = friendService.sendRequest(profile.getId(), addresseeId);

        return friendship;
    }

    @PutMapping("/{requesterId}/friends/accept")
    public Friendship acceptFriend(
            @AuthenticationPrincipal Profile profile,
            @PathVariable UUID requesterId
    ) {

        Friendship friendship = friendService.acceptRequest(requesterId, profile.getId());

        return friendship;
    }
}
