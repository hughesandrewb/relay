package com.andhug.relay.realtime.infrastructure.persistence;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.andhug.relay.realtime.domain.model.RealtimeTicket;
import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.realtime.domain.repository.RealtimeTicketRepository;
import com.andhug.relay.shared.domain.model.ProfileId;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RealtimeTicketRepositoryImpl implements RealtimeTicketRepository {

    private final StringRedisTemplate redisTemplate;

	@Override
	public ProfileId findByTicketCode(RealtimeTicketCode ticketCode) {
        return ProfileId.of(redisTemplate.opsForValue().get(ticketCode.getKey()));
	}

	@Override
	public void save(RealtimeTicket ticket) {
        redisTemplate.opsForValue().set(ticket.code().getKey(), ticket.profileId().toString(), ticket.ttl());
	}
}
