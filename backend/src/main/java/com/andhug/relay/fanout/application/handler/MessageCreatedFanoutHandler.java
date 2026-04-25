package com.andhug.relay.fanout.application.handler;

import com.andhug.relay.fanout.application.EventRouter;
import com.andhug.relay.fanout.application.resolver.MessageCreatedResolver;
import com.andhug.relay.message.domain.events.MessageCreatedEvent;
import com.andhug.relay.presence.application.service.PersenceService;
import com.andhug.relay.shared.domain.model.GatewayId;
import com.andhug.relay.shared.domain.model.OutboundEvent;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.ProfilePresence;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageCreatedFanoutHandler {

  private static final String EVENT_TYPE = "MESSAGE_CREATED";

  private final MessageCreatedResolver messageCreatedResolver;

  private final PersenceService persenceService;

  private final EventRouter eventRouter;

  private final ObjectMapper mapper;

  @ApplicationModuleListener
  void onMessageCreated(MessageCreatedEvent event) {
    log.info("Message created event received for message: {}", event.getMessageId());
    Set<ProfileId> toNotify = messageCreatedResolver.resolve(event);

    for (ProfileId profileId : toNotify) {
      ProfilePresence profilePresence = persenceService.getProfilePresence(profileId);

      for (GatewayId gatewayId : profilePresence.getGatewayIds()) {
        OutboundEvent outboundEvent =
            new OutboundEvent(profileId, EVENT_TYPE, mapper.valueToTree(event));
        log.info("{} {}", gatewayId.toString(), outboundEvent.toString());
        eventRouter.forward(gatewayId, outboundEvent);
      }
    }
  }
}
