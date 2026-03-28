package com.andhug.relay.profile.application.mapper;

import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.infrastructure.persistence.ProfileEntity;
import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;
import com.andhug.relay.shared.application.mapper.ValueObjectMapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { ValueObjectMapper.class })
public interface ProfileMapper {

    Profile toDomain(ProfileEntity entity);

    ProfileEntity toEntity(Profile domain);

    ProfileDto toDto(Profile domain);

    void updateEntity(Profile domain, @MappingTarget ProfileEntity entity);
}
