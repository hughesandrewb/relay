package com.andhug.relay.realtime.domain.model;

public abstract class Connection {
  public abstract void sendMessage(Object payload);

  public abstract boolean isActive();

  public abstract void close();
}
