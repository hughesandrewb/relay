package com.andhug.relay.message.application.command;

import com.andhug.relay.message.domain.model.MessageId;
import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import lombok.Builder;

@Builder
public record CreateMessageCommand(ProfileId authorId, RoomId roomId, String content)
    implements SyncCommand<MessageId> {}
