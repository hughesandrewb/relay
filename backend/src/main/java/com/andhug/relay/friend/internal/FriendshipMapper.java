package com.andhug.relay.friend.internal;

import com.andhug.relay.friend.api.models.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {

    @Mapping(target = "requesterId", source = "id.requesterId")
    @Mapping(target = "addresseeId", source = "id.addresseeId")
    Friendship toDomain(FriendshipEntity entity);
}
