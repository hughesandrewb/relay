package com.andhug.relay.shared.application.handler;

import com.andhug.relay.shared.application.command.AsyncCommand;

public interface AsyncCommandHandler<C extends AsyncCommand> {
  void handle(C command);
}
