package com.andhug.relay.message.application.handler;

import com.andhug.relay.message.application.service.MessageQueryService;
import com.andhug.relay.message.domain.events.MessageCreatedEvent;
import com.andhug.relay.message.infrastructure.web.dto.MessageDto;
import com.andhug.relay.realtime.application.domain.model.GatewayEvent;
import com.andhug.relay.realtime.application.service.NotificationDirector;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageCreatedHandler {

  private final NotificationDirector notificationDirector;

  private final MessageQueryService messageQueryService;

  @ApplicationModuleListener
  public void handle(MessageCreatedEvent event) {
    log.info("MessageCreatedEvent received for message: {}", event.getMessageId());

    MessageDto messageDto = messageQueryService.getMessage(event.getMessageId());

    notificationDirector.dispatch(List.of(), GatewayEvent.MESSAGE_CREATE, messageDto);
  }
}
