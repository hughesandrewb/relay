package com.andhug.relay.realtime;

import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.realtime.dto.RealtimeTicketDto;
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

    private final RealtimeTicketService realtimeTicketService;

    @GetMapping("/ticket")
    public ResponseEntity<RealtimeTicketDto> getRealtimeTicket(
            @AuthenticationPrincipal Profile profile) {

        String realtimeTicket = realtimeTicketService.getRealtimeTicket(profile);

        return ResponseEntity.ok(
                RealtimeTicketDto.builder()
                        .code(realtimeTicket)
                        .build());
    }
}
