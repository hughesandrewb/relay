package com.andhug.relay.profile.domain.repository;

import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.List;

public interface ProfileRepository {
  Profile findById(ProfileId profileId);

  List<Profile> findAllById(List<ProfileId> profileIds);

  List<Profile> getProfiles(List<ProfileId> profileIds);

  Profile save(Profile profile);
}
