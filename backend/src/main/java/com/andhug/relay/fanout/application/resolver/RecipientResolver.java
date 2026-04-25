package com.andhug.relay.fanout.application.resolver;

import com.andhug.relay.shared.domain.event.DomainEvent;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Set;

public interface RecipientResolver<T extends DomainEvent> {
  Class<T> supports();

  Set<ProfileId> resolve(T event);
}
