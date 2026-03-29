package com.andhug.relay.realtime.infrastructure.web;

import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.realtime.application.command.CreateRealtimeTicketCommand;
import com.andhug.relay.realtime.application.handler.CreateRealtimeTicketHandler;
import com.andhug.relay.realtime.infrastructure.web.dto.RealtimeTicketDto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/realtime")
@RequiredArgsConstructor
public class RealtimeTicketController {

    private final CreateRealtimeTicketHandler createRealtimeTicketHandler;

    @GetMapping("/ticket")
    public ResponseEntity<RealtimeTicketDto> getRealtimeTicket(
        @AuthenticationPrincipal Profile profile
    ) {

        var createRealtimeTicketCommand = CreateRealtimeTicketCommand.of(profile.getId());

        RealtimeTicketDto ticket = createRealtimeTicketHandler.handle(createRealtimeTicketCommand);

        return ResponseEntity
            .ok(ticket);
    }
}
