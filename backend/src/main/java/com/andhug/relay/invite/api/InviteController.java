package com.andhug.relay.invite.api;

import com.andhug.relay.invite.api.dto.response.AcceptInviteResponse;
import com.andhug.relay.invite.api.dto.response.GetInviteDetailsResponse;
import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.ProfileService;
import com.andhug.relay.profile.internal.ProfileMapper;
import com.andhug.relay.workspace.api.Workspace;
import com.andhug.relay.workspace.api.WorkspaceService;
import com.andhug.relay.workspace.internal.WorkspaceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Invite Controller", description = "APIs for managing invites")
public class InviteController {

    private final InviteService inviteService;

    private final WorkspaceService workspaceService;

    private final ProfileService profileService;

    private final WorkspaceMapper workspaceMapper;

    private final ProfileMapper profileMapper;

    @GetMapping("/{inviteId}")
    @Operation(summary = "Get information about invite", description = "Returns information about invite from invite ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite found"),
            @ApiResponse(responseCode = "404", description = "Invite not found")
    })
    public ResponseEntity<GetInviteDetailsResponse> getInvite(
            @Parameter(required = true)
            @PathVariable String inviteId) {

        Invite invite = inviteService.getInvite(inviteId);

        Profile sender = profileService.getProfile(invite.getSenderId());

        Workspace workspace = workspaceService.findById(invite.getWorkspaceId());

        return ResponseEntity.ok(GetInviteDetailsResponse.builder()
                .workspace(workspaceMapper.toDto(workspace))
                .sender(profileMapper.toDto(sender))
                .expiresAt(invite.getExpiresAt())
                .createdAt(invite.getCreatedAt())
                .build());
    }

    @PostMapping("/{inviteId}")
    @Operation(summary = "Accept invite", description = "Accept invite from unique invite identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted invite successfully")
    })
    public ResponseEntity<AcceptInviteResponse> acceptInvite(
            @Parameter(required = true)
            @PathVariable String inviteId) {

        log.info("Accepting invite {}", inviteId);

        inviteService.acceptInvite(inviteId);

        return ResponseEntity.ok(AcceptInviteResponse.builder()
                .build());
    }
}
