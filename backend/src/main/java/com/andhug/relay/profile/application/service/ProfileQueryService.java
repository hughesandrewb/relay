package com.andhug.relay.profile.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.andhug.relay.profile.application.mapper.ProfileMapper;
import com.andhug.relay.profile.domain.repository.ProfileRepository;
import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;
import com.andhug.relay.shared.domain.model.ProfileId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileQueryService {

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;
    
    public List<ProfileDto> getProfiles(List<ProfileId> profileIds) {
        return profileRepository
            .getProfiles(profileIds)
            .stream()
            .map(profileMapper::toDto)
            .toList();
    }

    public ProfileDto getProfile(ProfileId profileId) {
        return profileMapper.toDto(profileRepository.findById(profileId));
    }
}
