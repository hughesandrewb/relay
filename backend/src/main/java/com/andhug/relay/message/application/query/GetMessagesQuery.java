package com.andhug.relay.message.application.query;

import java.util.Objects;
import java.util.UUID;

import com.andhug.relay.message.domain.model.MessageId;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;

/**
 * A query for retrieving messages from a room.
 */
public record GetMessagesQuery(
    UUID roomId,
    MessageId beforeId,
    MessageId afterId,
    int limit
) {

    private static final int DEFAULT_LIMIT = 50;

    /**
     * Constructs a new GetMessagesQuery.
     * 
     * @param roomId the ID of the room to retrieve messages from
     * @param beforeId the ID of the message to retrieve messages before, mutually exclusive with afterId
     * @param afterId the ID of the message to retrieve messages after, mutually exclusive with beforeId
     * @param limit number of messages to retrieve, default is 50
     */
    public GetMessagesQuery {
        Objects.requireNonNull(roomId, "roomId is required");
        if (beforeId != null && afterId != null) {
            throw new InvalidArgumentException("beforeId and afterId are mutually exclusive");
        }
        if (limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
    }

    /**
     * Get the latest messages in a room.
     * 
     * @param roomId the ID of the room to retrieve messages from
     * @param limit number of messages to retrieve, default is 50
     * @return a GetMessagesQuery instance for retrieving the latest messages in the specified room
     */
    public static GetMessagesQuery latest(UUID roomId, int limit) {
        return new GetMessagesQuery(roomId, null, null, limit);
    }

    /**
     * Get messages in a room before a specific message.
     * 
     * @param roomId the ID of the room to retrieve messages from
     * @param beforeId the ID of the message to retrieve messages before
     * @param limit number of messages to retrieve, default is 50
     * @return a GetMessagesQuery instance for retrieving messages before the specified message
     */
    public static GetMessagesQuery before(UUID roomId, MessageId beforeId, int limit) {
        return new GetMessagesQuery(roomId, beforeId, null, limit);
    }

    /**
     * Get messages in a room after a specific message.
     * 
     * @param roomId the ID of the room to retrieve messages from
     * @param afterId the ID of the message to retrieve messages after
     * @param limit number of messages to retrieve, default is 50
     * @return a GetMessagesQuery instance for retrieving messages after the specified message
     */
    public static GetMessagesQuery after(UUID roomId, MessageId afterId, int limit) {
        return new GetMessagesQuery(roomId, null, afterId, limit);
    }
}
