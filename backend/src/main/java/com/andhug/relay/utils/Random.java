package com.andhug.relay.utils;

import java.security.SecureRandom;

public class Random {

    public static String generateRandomCode(int length) {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        var random = new SecureRandom();
        var result = new StringBuilder(8);

        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }

        return result.toString();
    }
}
