package com.andhug.relay.friend.internal;

import com.andhug.relay.friend.api.dto.FriendSummaryDto;
import com.andhug.relay.friend.api.models.Friendship;
import com.andhug.relay.profile.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {

    @Mapping(target = "requesterId", source = "id.requesterId")
    @Mapping(target = "addresseeId", source = "id.addresseeId")
    Friendship toDomain(FriendshipEntity entity);

    FriendSummaryDto profileToFriendSummaryDto(Profile profile);
}
