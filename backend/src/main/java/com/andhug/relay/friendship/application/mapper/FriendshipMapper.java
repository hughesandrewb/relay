package com.andhug.relay.friendship.application.mapper;

import com.andhug.relay.friendship.domain.model.Friendship;
import com.andhug.relay.friendship.infrastructure.persistence.FriendshipEntity;
import com.andhug.relay.friendship.infrastructure.web.dto.FriendDto;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.shared.application.mapper.ValueObjectMapper;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {ValueObjectMapper.class})
public interface FriendshipMapper {
  Friendship toDomain(FriendshipEntity entity);

  FriendshipEntity toEntity(Friendship friendship);

  FriendDto profileToFriendSummaryDto(Profile profile);
}
