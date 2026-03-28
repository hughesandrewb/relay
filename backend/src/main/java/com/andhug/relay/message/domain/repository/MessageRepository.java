package com.andhug.relay.message.domain.repository;

import java.util.List;

import com.andhug.relay.message.application.query.GetMessagesQuery;
import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.domain.model.MessageId;

public interface MessageRepository {
    List<Message> findMessages(GetMessagesQuery query);
    Message findById(MessageId message);
    void save(Message message);
}
