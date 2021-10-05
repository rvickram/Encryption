package com.helloitsryan.encryption;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Enigma {

    public static Set<String> guessWords(String cipherText, String[] possibleWords) {
        Set<String> result = new HashSet<>(List.of(possibleWords));

        for (int i = 0; i < cipherText.length(); i++) {
            char c = cipherText.charAt(i);

            Set<String> temp = new HashSet<>();
            for (String word : result) {
                if (word.charAt(i) == c) {
                    temp.add(word);
                }
            }

            result.removeAll(temp);
        }

        return result;
    }
}
