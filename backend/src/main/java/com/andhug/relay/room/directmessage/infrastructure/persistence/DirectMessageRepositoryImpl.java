package com.andhug.relay.room.directmessage.infrastructure.persistence;

import com.andhug.relay.room.directmessage.domain.model.DirectMessage;
import com.andhug.relay.room.directmessage.domain.repository.DirectMessageRepository;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageRepositoryImpl implements DirectMessageRepository {

  @Override
  public List<DirectMessage> findByProfileId(ProfileId profileId) {
    // TODO: Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findByProfileId'");
  }

  @Override
  public void save(DirectMessage directMessage) {
    // TODO: Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }
}
