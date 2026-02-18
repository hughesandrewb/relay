package com.andhug.relay.utils;

import java.security.SecureRandom;

public class RandomUtils {

    public static final SecureRandom random = new SecureRandom();

    public static String generateRandomCode(int length) {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        var result = new StringBuilder(8);

        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }

        return result.toString();
    }

    public static String generateRandomColor(float minSaturation, float maxSaturation, float minLightness, float maxLightness) {

        float hue = random.nextFloat();
        float saturation = minSaturation + random.nextFloat() * (maxSaturation - minSaturation);
        float lightness = minLightness + random.nextFloat() * (maxLightness - minLightness);

        return hslToHex(hue, saturation, lightness);
    }

    public static String generateRandomColor() {

        return generateRandomColor(0.7f, 1.0f, 0.6f, 0.8f);
    }

    private static String hslToHex(float h, float s, float l) {

        float c = (1 - Math.abs(2 * l - 1)) * s;
        float x = c * (1 - Math.abs((h * 6) % 2 - 1));
        float m = l - c / 2;

        float r, g, b;
        int hueSegment = (int)(h * 6);

        switch(hueSegment) {
            case 0: r = c; g = x; b = 0; break;
            case 1: r = x; g = c; b = 0; break;
            case 2: r = 0; g = c; b = x; break;
            case 3: r = 0; g = x; b = c; break;
            case 4: r = x; g = 0; b = c; break;
            default: r = c; g = 0; b = x; break;
        }

        int red = Math.round((r + m) * 255);
        int green = Math.round((g + m) * 255);
        int blue = Math.round((b + m) * 255);

        return String.format("#%02X%02X%02X", red, green, blue);
    }
}
