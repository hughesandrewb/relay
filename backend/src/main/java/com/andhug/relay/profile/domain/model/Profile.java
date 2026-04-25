package com.andhug.relay.profile.domain.model;

import com.andhug.relay.shared.application.utils.RandomUtils;
import com.andhug.relay.shared.domain.model.AggregateRoot;
import com.andhug.relay.shared.domain.model.Color;
import com.andhug.relay.shared.domain.model.DisplayName;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends AggregateRoot {

  private ProfileId id;

  private String username;

  private DisplayName displayName;

  private Color accentColor;

  public static Profile create(ProfileId id, String username) {
    if (id == null) {
      throw new IllegalArgumentException("id cannot be null");
    }
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("username cannot be null or blank");
    }
    var displayName = DisplayName.of(username);
    var accentColor = Color.of(RandomUtils.generateRandomColor());

    var profile = new Profile(id, username, displayName, accentColor);
    // TODO: register profile created event

    return profile;
  }
}
