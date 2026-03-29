package com.andhug.relay.profile.infrastructure.web.dto;

import java.util.UUID;

public record ProfileDto(UUID id, String username, String displayName, String accentColor) {}
