package com.andhug.relay.invite.api;

import com.andhug.relay.invite.api.dto.InviteDto;
import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.ProfileService;
import com.andhug.relay.profile.internal.ProfileMapper;
import com.andhug.relay.workspace.api.Workspace;
import com.andhug.relay.workspace.api.WorkspaceService;
import com.andhug.relay.workspace.internal.WorkspaceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @GetMapping("/{invite-code}")
    @Operation(summary = "Get information about invite", description = "Returns information about invite from invite ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite found"),
            @ApiResponse(responseCode = "404", description = "Invite not found")
    })
    public ResponseEntity<InviteDto> getInvite(
            @Parameter(in = ParameterIn.PATH, required = true, name = "invite-code", schema = @Schema(type = "string"))
            @PathVariable("invite-code") String inviteCode) {

        Invite invite = inviteService.getInvite(inviteCode);

        Profile sender = profileService.getProfile(invite.getSenderId());

        Workspace workspace = workspaceService.findById(invite.getWorkspaceId());

        return ResponseEntity.ok(InviteDto.builder()
                .workspace(workspaceMapper.toDto(workspace))
                .sender(profileMapper.toDto(sender))
                .expiresAt(invite.getExpiresAt())
                .createdAt(invite.getCreatedAt())
                .build());
    }

    @PostMapping("/{invite-code}")
    @Operation(summary = "Accept invite", description = "Accept invite from unique invite identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted invite successfully")
    })
    public ResponseEntity<InviteDto> acceptInvite(
            @Parameter(in = ParameterIn.PATH, required = true, name = "invite-code", schema = @Schema(type = "string"))
            @PathVariable("invite-code") String inviteCode) {

        log.info("Accepting invite {}", inviteCode);

        inviteService.acceptInvite(inviteCode);

        return ResponseEntity.ok(InviteDto.builder().build());
    }
}
