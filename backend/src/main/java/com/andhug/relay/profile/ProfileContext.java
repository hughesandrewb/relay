package com.andhug.relay.profile;

public class ProfileContext {

    private static final ThreadLocal<Profile> currentProfile = new ThreadLocal<>();

    public static Profile getCurrentProfile() {

        Profile profile = currentProfile.get();
        if (profile == null) {
            // throw new UnauthorizedException("No authenticated profile in context");
        }
        return profile;
    }

    public static void setCurrentProfile(Profile profile) {

        currentProfile.set(profile);
    }

    public static void clear() {
        currentProfile.remove();
    }
}
