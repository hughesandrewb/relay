package com.andhug.relay.fanout.application.resolver;

import com.andhug.relay.message.domain.events.MessageCreatedEvent;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class MessageCreatedResolver implements RecipientResolver<MessageCreatedEvent> {

  @Override
  public Class<MessageCreatedEvent> supports() {
    return MessageCreatedEvent.class;
  }

  @Override
  public Set<ProfileId> resolve(MessageCreatedEvent event) {
    return Set.of(ProfileId.of("ccddf6bf-3ac3-4f92-9ee0-a9f396c97f17"));
  }
}
