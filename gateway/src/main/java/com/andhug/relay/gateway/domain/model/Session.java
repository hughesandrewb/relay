package com.andhug.relay.gateway.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;
import tools.jackson.databind.JsonNode;

@Slf4j
@Getter
public class Session {

  private static final int EVENT_REPLAY_BUFFER_SIZE = 128;

  private final String sessionId;

  private final Sinks.Many<Event<Object>> messageSink =
      Sinks.many().unicast().onBackpressureBuffer();

  private final LinkedBlockingDeque<Event<Object>> eventBuffer =
      new LinkedBlockingDeque<>(EVENT_REPLAY_BUFFER_SIZE);

  private final AtomicInteger sequenceNumber = new AtomicInteger(0);

  public Session(String sessionId) {
    this.sessionId = sessionId;
  }

  public void sendEvent(String type, JsonNode data) {

    synchronized (this) {
      int sequence = sequenceNumber.getAndIncrement();
      Event<Object> event = Event.of(Opcode.DISPATCH, data, sequence, type);
      bufferEvent(event);
      messageSink.tryEmitNext(event);
    }
  }

  public void replayFrom(int lastSequenceNumber) {

    synchronized (this) {
      if (lastSequenceNumber < oldestSequence()) {
        throw new IllegalStateException(
            "Cannot replay events: requested sequence "
                + lastSequenceNumber
                + " is older than oldest buffered event with sequence "
                + oldestSequence());
      }
      List<Event<Object>> snapshot = new ArrayList<>(eventBuffer);
      snapshot.stream()
          .filter(event -> event.sequence() > lastSequenceNumber)
          .forEach(event -> messageSink.tryEmitNext(event));
    }
  }

  private void bufferEvent(Event<Object> event) {

    if (eventBuffer.size() >= EVENT_REPLAY_BUFFER_SIZE) {
      eventBuffer.pollFirst();
    }
    eventBuffer.offerLast(event);
  }

  private int oldestSequence() {
    Event<Object> oldestEvent = eventBuffer.peekFirst();
    return oldestEvent != null ? oldestEvent.sequence() : 0;
  }
}
