package com.andhug.relay.realtime;

import com.andhug.relay.profile.Profile;
import com.andhug.relay.utils.Constants;
import com.andhug.relay.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealtimeTicketService {

    private final StringRedisTemplate redisTemplate;

    public String getRealtimeTicket(Profile profile) {

        return getRealtimeTicket(profile, Constants.REALTIME_TICKET_DURATION_SECONDS);
    }

    public String getRealtimeTicket(Profile profile, int ttlDuration) {

        String ticketCode = generateRealtimeTicketCode();
        String key = "ws:ticket:" + ticketCode;

        redisTemplate.opsForValue().set(key, profile.getId().toString(), Duration.ofSeconds(ttlDuration));

        return ticketCode;
    }

    public Optional<UUID> validateRealtimeTicket(String ticketCode) {

        log.info("Validating ticket code: {}", ticketCode);

        String key = "ws:ticket:" + ticketCode;
        String profileIdString = redisTemplate.opsForValue().get(key);

        if (profileIdString == null) {
            return Optional.empty();
        }

        UUID profileId = UUID.fromString(profileIdString);

        return Optional.of(profileId);
    }

    private String generateRealtimeTicketCode() {
        return RandomUtils.generateRandomCode(Constants.REALTIME_TICKET_CODE_LENGTH);
    }
}
