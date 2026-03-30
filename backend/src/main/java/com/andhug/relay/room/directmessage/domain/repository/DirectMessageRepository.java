package com.andhug.relay.room.directmessage.domain.repository;

import com.andhug.relay.room.directmessage.domain.model.DirectMessage;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import java.util.List;

public interface DirectMessageRepository {
  List<DirectMessage> findByProfileId(ProfileId profileId);

  DirectMessage findById(RoomId roomId);

  void save(DirectMessage directMessage);
}
