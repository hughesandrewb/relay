package com.andhug.relay.workspace.api;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace {

    private UUID id;

    private String name;

    private UUID ownerId;
}
