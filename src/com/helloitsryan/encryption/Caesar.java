package com.helloitsryan.encryption;

public class Caesar {

    public static String encrypt(String baseText, int rotation) {
        StringBuilder result = new StringBuilder();

        for (char c : baseText.toCharArray()) {
            int distanceFromZ = 'z' - c;

            if ((char)(c+rotation) > 'z') {
                result.append((char)('a' + rotation - 1 - distanceFromZ));
            } else {
                result.append((char)(c+rotation));
            }
        }

        return result.toString();
    }
}
