package com.andhug.relay.profile.infrastructure.persistence;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.andhug.relay.profile.application.mapper.ProfileMapper;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.domain.repository.ProfileRepository;
import com.andhug.relay.shared.domain.exception.NotFoundException;
import com.andhug.relay.shared.domain.model.ProfileId;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfileJpaRepository profileJpaRepository;

    private final ProfileMapper profileMapper;

    @Override
    public Profile findById(ProfileId profileId) {
        ProfileEntity profileEntity = profileJpaRepository
            .findById(profileId.value())
            .orElseThrow(() -> new NotFoundException(String.format("Profile could not be found, id: %s", profileId.toString())));

        return profileMapper.toDomain(profileEntity);
    }

	@Override
	public List<Profile> getProfiles(List<ProfileId> profileIds) {
        Set<UUID> uuids = profileIds
            .stream()
            .map(ProfileId::value)
            .collect(Collectors.toSet());

        return profileJpaRepository
            .findAllById(uuids)
            .stream()
            .map(profileMapper::toDomain)
            .toList();
	}

    @Override
	public Profile save(Profile profile) {
        ProfileEntity profileEntity = profileMapper.toEntity(profile);

        profileJpaRepository.save(profileEntity);

        return profileMapper.toDomain(profileEntity);
	}
}
