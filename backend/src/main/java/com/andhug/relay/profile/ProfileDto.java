package com.andhug.relay.profile;

import java.util.UUID;

public record ProfileDto(
        UUID id,
        String username,
        String displayName
) {}
