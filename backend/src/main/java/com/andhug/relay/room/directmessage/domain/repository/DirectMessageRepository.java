package com.andhug.relay.room.directmessage.domain.repository;

import java.util.List;

import com.andhug.relay.room.directmessage.domain.model.DirectMessage;
import com.andhug.relay.shared.domain.model.ProfileId;

public interface DirectMessageRepository {
    List<DirectMessage> findByProfileId(ProfileId profileId);
    void save(DirectMessage directMessage);
}
