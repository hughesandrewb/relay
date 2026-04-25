package com.andhug.relay.message.domain.repository;

import com.andhug.relay.message.application.query.GetMessagesQuery;
import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.shared.domain.model.MessageId;
import java.util.List;

public interface MessageRepository {
  List<Message> findMessages(GetMessagesQuery query);

  Message findById(MessageId message);

  void save(Message message);
}
