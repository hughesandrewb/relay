package com.andhug.relay.message.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Message {

    private final long id;

    private final UUID authorId;

    private final UUID roomId;

    private String content;
}
