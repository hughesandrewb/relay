package com.andhug.relay.shared.application.handler;

import com.andhug.relay.shared.application.command.SyncCommand;

public interface SyncCommandHandler<C extends SyncCommand<R>, R> {
  R handle(C command);
}
