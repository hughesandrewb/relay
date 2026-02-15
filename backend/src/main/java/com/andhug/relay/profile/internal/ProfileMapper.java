package com.andhug.relay.profile.internal;

import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.ProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile toDomain(ProfileEntity entity);

    ProfileEntity toEntity(Profile domain);

    ProfileDto toDto(Profile domain);

    void updateEntity(Profile domain, @MappingTarget ProfileEntity entity);
}
