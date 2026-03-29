package com.andhug.relay.shared.application.mapper;

import com.andhug.relay.friendship.domain.model.FriendshipId;
import com.andhug.relay.invite.domain.model.InviteCode;
import com.andhug.relay.invite.domain.model.InviteId;
import com.andhug.relay.message.domain.model.MessageId;
import com.andhug.relay.profile.domain.model.DisplayName;
import com.andhug.relay.shared.domain.model.Color;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.shared.domain.model.WorkspaceMemberId;
import com.andhug.relay.workspacemembership.domain.model.Nickname;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ValueObjectMapper {

  default DisplayName toDisplayName(String displayName) {
    return DisplayName.of(displayName);
  }

  default String fromDisplayName(DisplayName displayName) {
    return displayName.value();
  }

  default Color toColor(Integer color) {
    if (color == null) {
      return null;
    }
    return Color.of(color);
  }

  default Integer fromColorToInteger(Color color) {
    if (color == null) {
      return null;
    }
    String hex = color.value().startsWith("#") ? color.value().substring(1) : color.value();
    return Integer.parseInt(hex, 16);
  }

  default String fromColorToString(Color color) {
    return color.value();
  }

  default ProfileId toProfileId(UUID id) {
    return ProfileId.of(id);
  }

  default UUID fromProfileId(ProfileId profileId) {
    return profileId.value();
  }

  default FriendshipId toFriendshipId(UUID id) {
    return FriendshipId.of(id);
  }

  default UUID fromFriendshipId(FriendshipId friendshipId) {
    return friendshipId.value();
  }

  default InviteId toInviteId(UUID id) {
    return InviteId.of(id);
  }

  default UUID fromInviteId(InviteId inviteId) {
    return inviteId.value();
  }

  default WorkspaceId toWorkspaceId(UUID id) {
    return WorkspaceId.of(id);
  }

  default UUID fromWorkspaceId(WorkspaceId workspaceId) {
    return workspaceId.value();
  }

  default WorkspaceMemberId toWorkspaceMemberId(UUID id) {
    return WorkspaceMemberId.of(id);
  }

  default UUID fromWorkspaceMemberId(WorkspaceMemberId workspaceMemberId) {
    return workspaceMemberId.value();
  }

  default RoomId toRoomId(UUID id) {
    return RoomId.of(id);
  }

  default UUID fromRoomId(RoomId roomId) {
    return roomId.value();
  }

  default MessageId toMessageId(Long id) {
    return MessageId.of(id);
  }

  default Long fromMessageId(MessageId messageId) {
    return messageId.value();
  }

  default Nickname toNickname(String nickname) {
    return Nickname.of(nickname);
  }

  default String fromNickname(Nickname nickname) {
    return nickname.value();
  }

  default InviteCode toInviteCode(String inviteCode) {
    return InviteCode.of(inviteCode);
  }

  default String fromInviteCode(InviteCode inviteCode) {
    return inviteCode.toString();
  }
}
