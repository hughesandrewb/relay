package com.andhug.relay.profile.domain.model;

import lombok.*;

import com.andhug.relay.shared.domain.model.AggregateRoot;
import com.andhug.relay.shared.domain.model.Color;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.utils.RandomUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends AggregateRoot {

    private ProfileId id;

    private String username;

    private DisplayName displayName;

    private Color accentColor;

    public static Profile create(ProfileId id, String username, DisplayName displayName, Color accentColor) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username cannot be null or blank");
        }
        if (displayName == null) {
            displayName = DisplayName.of(username);
        }
        if (accentColor == null) {
            accentColor = Color.of(RandomUtils.generateRandomColor());
        }

        var profile = new Profile(id, username, displayName, accentColor);
        // TODO: register profile created event

        return profile;
    }
}
