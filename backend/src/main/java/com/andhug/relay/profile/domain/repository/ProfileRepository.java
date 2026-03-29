package com.andhug.relay.profile.domain.repository;

import java.util.List;

import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.shared.domain.model.ProfileId;

public interface ProfileRepository {
    Profile findById(ProfileId profileId);
    List<Profile> findAllById(List<ProfileId> profileIds);
    List<Profile> getProfiles(List<ProfileId> profileIds);
    Profile save(Profile profile);
}
