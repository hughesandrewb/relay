package com.andhug.relay.room.directmessage.infrastructure.persistence;

import com.andhug.relay.room.directmessage.domain.model.DirectMessage;
import com.andhug.relay.room.directmessage.domain.repository.DirectMessageRepository;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectMessageRepositoryImpl implements DirectMessageRepository {

  @Override
  public List<DirectMessage> findByProfileId(ProfileId profileId) {
    // TODO: Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findByProfileId'");
  }

  @Override
  public DirectMessage findById(RoomId roomId) {
    // TODO: Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public void save(DirectMessage directMessage) {
    // TODO: Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }
}
