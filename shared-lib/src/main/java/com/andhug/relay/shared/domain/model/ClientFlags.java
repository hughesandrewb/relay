package com.andhug.relay.shared.domain.model;

public class ClientFlags {

  private static final int WEB_FLAG = 1; // 0001
  private static final int DESKTOP_FLAG = 2; // 0010
  private static final int MOBILE_FLAG = 4; // 0100

  private int flags;

  private ClientFlags(int flags) {
    this.flags = flags;
  }

  public static ClientFlags of(int flags) {
    return new ClientFlags(flags);
  }

  public static ClientFlags of(boolean isMobile, boolean isWeb, boolean isDesktop) {
    int flags = 0;
    if (isWeb) {
      flags |= WEB_FLAG;
    }
    if (isDesktop) {
      flags |= DESKTOP_FLAG;
    }
    if (isMobile) {
      flags |= MOBILE_FLAG;
    }
    return new ClientFlags(flags);
  }

  public boolean webActive() {
    return (flags & WEB_FLAG) != 0;
  }

  public boolean desktopActive() {
    return (flags & DESKTOP_FLAG) != 0;
  }

  public boolean mobileActive() {
    return (flags & MOBILE_FLAG) != 0;
  }

  public void setWeb(boolean isWeb) {
    if (isWeb) {
      flags |= WEB_FLAG;
    } else {
      flags &= ~WEB_FLAG;
    }
  }

  public void setDesktop(boolean isDesktop) {
    if (isDesktop) {
      flags |= DESKTOP_FLAG;
    } else {
      flags &= ~DESKTOP_FLAG;
    }
  }

  public void setMobile(boolean isMobile) {
    if (isMobile) {
      flags |= MOBILE_FLAG;
    } else {
      flags &= ~MOBILE_FLAG;
    }
  }

  public int getFlags() {
    return flags;
  }
}
