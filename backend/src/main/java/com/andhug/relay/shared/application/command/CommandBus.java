package com.andhug.relay.shared.application.command;

public interface CommandBus {
  <R> R dispatch(SyncCommand<R> command);

  void dispatch(AsyncCommand command);
}
