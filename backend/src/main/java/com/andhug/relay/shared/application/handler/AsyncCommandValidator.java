package com.andhug.relay.shared.application.handler;

import com.andhug.relay.shared.application.command.AsyncCommand;

/**
 * AsyncCommandValidator is an interface for validating asynchronous commands before they are
 * handled by an AsyncCommandHandler. Implementations of this interface should contain the logic to
 * validate the command's data and throw appropriate exceptions if the command is invalid. This
 * allows for separation of concerns, where the validation logic is kept separate from the command
 * handling logic.
 */
public interface AsyncCommandValidator<C extends AsyncCommand> {
  void validate(C command);
}
