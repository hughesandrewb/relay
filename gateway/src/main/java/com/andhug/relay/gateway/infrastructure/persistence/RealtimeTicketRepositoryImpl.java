package com.andhug.relay.gateway.infrastructure.persistence;

import com.andhug.relay.realtime.domain.model.RealtimeTicket;
import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.realtime.domain.repository.RealtimeTicketRepository;
import com.andhug.relay.shared.domain.exception.NotFoundException;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RealtimeTicketRepositoryImpl implements RealtimeTicketRepository {

  private final StringRedisTemplate redisTemplate;

  @Override
  public ProfileId findByTicketCode(RealtimeTicketCode ticketCode) {
    ProfileId res;
    try {
      res = ProfileId.of(redisTemplate.opsForValue().get(ticketCode.getKey()));
    } catch (Exception e) {
      throw new NotFoundException("Could not find ticket: " + ticketCode.toString());
    }
    log.info("Found ticket: {} {}", ticketCode.toString(), res.toString());
    return res;
  }

  @Override
  public void save(RealtimeTicket ticket) {
    redisTemplate
        .opsForValue()
        .set(ticket.code().getKey(), ticket.profileId().toString(), ticket.ttl());
  }

  @Override
  public void delete(RealtimeTicketCode ticketCode) {
    redisTemplate.delete(ticketCode.getKey());
  }
}
