package com.andhug.relay.invite.infrastructure.web;

import com.andhug.relay.invite.application.command.AcceptInviteCommand;
import com.andhug.relay.invite.application.handler.AcceptInviteHandler;
import com.andhug.relay.invite.application.service.InviteQueryService;
import com.andhug.relay.invite.domain.model.InviteCode;
import com.andhug.relay.invite.infrastructure.web.dto.InviteDto;
import com.andhug.relay.profile.domain.model.Profile;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Invite Controller", description = "APIs for managing invites")
public class InviteController {

    private final AcceptInviteHandler acceptInviteHandler;

    private final InviteQueryService inviteQueryService;

    @GetMapping("/{invite-code}")
    @Operation(summary = "Get information about invite", description = "Returns information about invite from invite ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite found"),
            @ApiResponse(responseCode = "404", description = "Invite not found")
    })
    public ResponseEntity<InviteDto> getInvite(
            @Parameter(in = ParameterIn.PATH, required = true, name = "invite-code", schema = @Schema(type = "string"))
            @PathVariable("invite-code") String inviteCode) {

        InviteDto invite = inviteQueryService.getInvite(InviteCode.of(inviteCode));

        return ResponseEntity.ok(invite);
    }

    @PostMapping("/{invite-code}")
    @Operation(summary = "Accept invite", description = "Accept invite from unique invite identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted invite successfully")
    })
    public ResponseEntity<Void> acceptInvite(
            @AuthenticationPrincipal Profile profile,
            @Parameter(in = ParameterIn.PATH, required = true, name = "invite-code", schema = @Schema(type = "string"))
            @PathVariable("invite-code") String inviteCode) {

        var acceptInviteCommand = AcceptInviteCommand.builder()
                .code(InviteCode.of(inviteCode))
                .profileId(profile.getId())
                .build();

        acceptInviteHandler.handle(acceptInviteCommand);
        

        return ResponseEntity.ok().build();
    }
}
