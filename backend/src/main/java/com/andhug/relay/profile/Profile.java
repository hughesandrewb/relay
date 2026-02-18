package com.andhug.relay.profile;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    private UUID id;

    private String username;

    private String displayName;

    private String accentColor;
}
