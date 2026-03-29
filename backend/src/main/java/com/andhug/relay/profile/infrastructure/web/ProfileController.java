package com.andhug.relay.profile.infrastructure.web;

import com.andhug.relay.friendship.application.command.AcceptFriendRequestCommand;
import com.andhug.relay.friendship.application.command.SendFriendRequestCommand;
import com.andhug.relay.friendship.application.service.FriendshipCommandService;
import com.andhug.relay.friendship.application.service.FriendshipQueryService;
import com.andhug.relay.friendship.infrastructure.web.dto.FriendDto;
import com.andhug.relay.profile.application.mapper.ProfileMapper;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;
import com.andhug.relay.room.directmessage.application.command.CreateDirectMessageCommand;
import com.andhug.relay.room.directmessage.application.service.DirectMessageCommandService;
import com.andhug.relay.room.directmessage.application.service.DirectMessageQueryService;
import com.andhug.relay.room.directmessage.infrastructure.web.dto.DirectMessageDto;
import com.andhug.relay.room.infrastructure.web.dto.RoomDto;
import com.andhug.relay.room.infrastructure.web.dto.request.CreateDirectMessageRequest;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.workspace.infrastructure.web.dto.WorkspaceDto;
import com.andhug.relay.workspacemembership.application.service.WorkspaceMemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Validated
@Tag(name = "Profile Controller", description = "APIs for managing profiles")
public class ProfileController {

  private final WorkspaceMemberQueryService workspaceMemberQueryService;

  private final FriendshipCommandService friendshipCommandService;

  private final FriendshipQueryService friendshipQueryService;

  private final DirectMessageQueryService directMessageQueryService;

  private final DirectMessageCommandService directMessageCommandService;

  private final ProfileMapper profileMapper;

  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get current profile's information",
      description = "Returns the current profile's information")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Profile information"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  public ProfileDto getMe(@AuthenticationPrincipal Profile profile) {

    return profileMapper.toDto(profile);
  }

  @GetMapping(value = "/me/workspaces", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get current profile's workspaces",
      description = "Returns a list of workspaces that the current profile is a member of")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "List of current profile's workspaces"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  public List<WorkspaceDto> getWorkspaces(@AuthenticationPrincipal Profile profile) {

    List<WorkspaceDto> workspaces =
        workspaceMemberQueryService.getWorkspacesOfProfile(profile.getId());

    return workspaces;
  }

  @GetMapping(value = "/me/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get current profile's direct messages",
      description =
          "Returns a list of direct message objects that the current profile is a participant of")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of current profile's direct messages")
      })
  public List<DirectMessageDto> getDirectMessages(@AuthenticationPrincipal Profile profile) {

    return directMessageQueryService.getDirectMessage(profile.getId());
  }

  @PostMapping(
      value = "/me/rooms",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Create direct message room",
      description =
          "Creates a direct message room and includes current user and recipient as participants")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Created direct message room")})
  public RoomDto createDirectMessage(
      @AuthenticationPrincipal Profile profile, @RequestBody CreateDirectMessageRequest request) {

    var createDirectMessageCommand =
        CreateDirectMessageCommand.of(profile.getId(), ProfileId.of(request.recipientId()));

    directMessageCommandService.createDirectMessage(createDirectMessageCommand);

    return RoomDto.builder().build();
  }

  @GetMapping(value = "/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<FriendDto> getFriends(@PathVariable UUID id) {

    return friendshipQueryService.getFriends(id);
  }

  @GetMapping(value = "/me/friends")
  public List<FriendDto> getFriends(@AuthenticationPrincipal Profile profile) {

    return getFriends(profile.getId().value());
  }

  @PostMapping(value = "/{addresseeId}/friends/request")
  public ResponseEntity<Void> sendFriendRequest(
      @AuthenticationPrincipal Profile profile, @PathVariable UUID addresseeId) {

    var sendFriendRequestCommand =
        SendFriendRequestCommand.builder()
            .requesterId(profile.getId())
            .addresseeId(ProfileId.of(addresseeId))
            .build();

    friendshipCommandService.sendFriendRequest(sendFriendRequestCommand);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{requesterId}/friends/accept")
  public ResponseEntity<Void> acceptFriend(
      @AuthenticationPrincipal Profile profile, @PathVariable UUID requesterId) {
    var acceptFriendRequestCommand =
        AcceptFriendRequestCommand.builder()
            .requesterId(ProfileId.of(requesterId))
            .addresseeId(profile.getId())
            .build();

    friendshipCommandService.acceptFriendRequest(acceptFriendRequestCommand);

    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }
}
