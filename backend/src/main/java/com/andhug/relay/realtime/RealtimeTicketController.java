package com.andhug.relay.realtime;

import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.ProfileContext;
import com.andhug.relay.realtime.dto.RealtimeTicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RealtimeTicketController {

    private final RealtimeTicketService realtimeTicketService;

    @GetMapping("/api/realtime/ticket")
    public ResponseEntity<RealtimeTicketDto> getRealtimeTicket() {

        Profile currentProfile = ProfileContext.getCurrentProfile();

        String realtimeTicket = realtimeTicketService.getRealtimeTicket(currentProfile);

        return ResponseEntity.ok(
                RealtimeTicketDto.builder()
                        .code(realtimeTicket)
                        .build());
    }
}
