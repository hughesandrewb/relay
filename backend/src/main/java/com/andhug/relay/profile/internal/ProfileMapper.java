package com.andhug.relay.profile.internal;

import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.ProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(source = "accentColor", target = "accentColor", qualifiedByName = "intToColorString")
    Profile toDomain(ProfileEntity entity);

    @Mapping(source = "accentColor", target = "accentColor", qualifiedByName = "colorStringToInt")
    ProfileEntity toEntity(Profile domain);

    ProfileDto toDto(Profile domain);

    void updateEntity(Profile domain, @MappingTarget ProfileEntity entity);

    @Named("intToColorString")
    default String intToColorString(Integer value) {
        if (value == null) {
            return null;
        }
        return String.format("%06X", value);
    }

    @Named("colorStringToInt")
    default Integer colorStringToInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        String hex = value.startsWith("#") ? value.substring(1) : value;
        return Integer.parseInt(hex, 16);
    }
}
