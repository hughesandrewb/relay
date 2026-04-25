package com.andhug.relay.realtime.application.domain.model;

public abstract class Connection {
  public abstract void sendMessage(Object payload);

  public abstract boolean isActive();

  public abstract void close();
}
