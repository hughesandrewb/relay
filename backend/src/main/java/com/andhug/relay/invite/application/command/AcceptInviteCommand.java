package com.andhug.relay.invite.application.command;

import com.andhug.relay.invite.domain.model.InviteCode;
import com.andhug.relay.shared.application.command.AsyncCommand;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.Builder;

@Builder
public record AcceptInviteCommand(InviteCode code, ProfileId profileId) implements AsyncCommand {}
