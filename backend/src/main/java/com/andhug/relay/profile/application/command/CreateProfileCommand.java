package com.andhug.relay.profile.application.command;

import com.andhug.relay.profile.domain.model.DisplayName;
import com.andhug.relay.shared.domain.model.Color;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Optional;
import lombok.Builder;

@Builder
public record CreateProfileCommand(
    ProfileId userId,
    String username,
    Optional<DisplayName> displayName,
    Optional<Color> accentColor) {}
