package com.helloitsryan.encryption;

import java.util.*;

public class BlockCipher {

    public static void main(String[] args) {
        int[] key = {162, 127, 100, 71, 3, 114, 178, 130,
                122, 189, 23, 214, 83, 153, 231, 145,
                47, 128, 224, 60, 115, 255, 137, 248,
                96, 81, 249, 45, 93, 213, 161, 136,
                191, 244, 142, 174, 194, 167, 172,
                141, 5, 242, 91, 225, 31, 37, 75, 61,
                82, 226, 26, 105, 230, 15, 209, 109,
                246, 144, 27, 29, 24, 40, 175, 158,
                103, 0, 73, 199, 120, 33, 180, 32, 121,
                211, 156, 79, 54, 185, 76, 43, 239, 234,
                222, 250, 63, 57, 6, 116, 99, 132, 14,
                232, 77, 62, 240, 183, 208, 126, 148, 106,
                66, 165, 52, 107, 147, 41, 123, 186, 55,
                200, 237, 151, 30, 129, 215, 69, 25, 152,
                117, 160, 212, 125, 150, 97, 188, 166, 48,
                217, 2, 8, 84, 124, 190, 28, 18, 49, 210,
                140, 184, 139, 227, 98, 95, 197, 235, 170,
                198, 205, 202, 241, 203, 169, 85, 207, 134,
                10, 110, 135, 87, 78, 181, 16, 253, 90, 70,
                146, 236, 176, 80, 72, 36, 113, 21, 164, 68, 12,
                182, 59, 157, 187, 159, 13, 46, 50, 119, 56, 17,
                254, 19, 65, 223, 58, 138, 177, 168, 131, 220, 39,
                155, 245, 74, 38, 111, 252, 179, 20, 67, 112, 64, 243,
                94, 206, 35, 163, 204, 11, 171, 195, 149, 247, 104, 218,
                221, 4, 89, 201, 251, 143, 51, 92, 216, 7, 229, 102, 228,
                9, 118, 53, 193, 173, 22, 219, 233, 1, 44, 101, 154, 86,
                196, 238, 133, 88, 42, 192, 34, 108};

        final BlockCipher blockCipher = new BlockCipher(key);

        int[] plaintext = { 2, 5, 0, 9, 2, 3, 2, 8, 2 };
        System.out.println("CBC (Cipher-Block-Chaining): "+Arrays.toString(
                blockCipher.encryptCipherBlockChaining(plaintext, 23)
        ));
        System.out.println("CTR (Counter-Mode): " + Arrays.toString(
                blockCipher.encryptCounterMode(plaintext, 51)
        ));

        Integer[][] ciphers = {
                {56, 16, 139, 19, 243, 149},
                {50, 20, 225, 125, 17, 71, 232, 15},
                {145, 98, 145, 159, 65, 234, 64, 73},
                {223, 212, 126, 171, 128, 159, 244, 153}
        };
        int[] input = {145, 19, 71, 73, 232, 149, 244, 153};
        System.out.println("Decrypt: " + blockCipher.decrypt(
                input,
                Map.of(
                        "melody", ciphers[0],
                        "subtract", ciphers[1],
                        "consider", ciphers[2],
                        "neighbor", ciphers[3]
                ),
                        111)
        );
    }

    private final int[] key;

    public BlockCipher(final int[] key) {
        this.key = key;
    }

    public int[] encryptCipherBlockChaining(int[] plaintext, int initVector) {
        int[] ciphertext = new int[plaintext.length];

        // convert block and IV to binary
        String binaryInitVector = Integer.toBinaryString(initVector);
        String binaryBlock = Integer.toBinaryString(plaintext[0]);

        // do xor and convert the output to base 10, lookup value in key and store
        int postXor = Integer.parseInt(xor(binaryInitVector, binaryBlock),2);
        ciphertext[0] = key[postXor];

        for (int i = 1; i < plaintext.length; i++) {
            // update the init vector (binary)
            binaryInitVector = Integer.toBinaryString(ciphertext[i-1]);
            binaryBlock = Integer.toBinaryString(plaintext[i]);

            postXor = Integer.parseInt(xor(binaryInitVector, binaryBlock),2);
            ciphertext[i] = key[postXor];
        }

        return ciphertext;
    }

    public int[] encryptCounterMode(int[] plaintext, int initVector) {
        int[] ciphertext = new int[plaintext.length];

        for (int i = 0; i < plaintext.length; i++) {
            int encodedInitVector = key[initVector];

            String binaryEncodedInitVector = Integer.toBinaryString(encodedInitVector);
            String binaryBlock = Integer.toBinaryString(plaintext[i]);

            int cipherBlock = Integer.parseInt(xor(binaryEncodedInitVector, binaryBlock), 2);
            ciphertext[i] = cipherBlock;

            // wrap around to 0 if we reach the end of the chars in key
            if (++initVector >= key.length) {
                initVector = initVector - (key.length - 1);
            }
        }

        return ciphertext;
    }

    public String decrypt(int[] input, Map<String, Integer[]> knownMessageCipher, int initVector) {
        // maps the encoded val -> decoded val
        Map<Integer, Integer> valueKeyMap = new HashMap<>();

        // build the key from known messages
        for (Map.Entry<String, Integer[]> pair : knownMessageCipher.entrySet()) {
            String plaintext = pair.getKey();
            Integer[] cipherText = pair.getValue();

            String binaryInitVector = Integer.toBinaryString(initVector);
            String binaryInput = Integer.toBinaryString(plaintext.charAt(0));

            int postXor = Integer.parseInt(xor(binaryInput, binaryInitVector), 2);
            valueKeyMap.put(cipherText[0], postXor);


            for (int i = 1; i < plaintext.length(); i++) {
                binaryInitVector = Integer.toBinaryString(cipherText[i-1]);
                binaryInput = Integer.toBinaryString(plaintext.charAt(i));

                postXor = Integer.parseInt(xor(binaryInput, binaryInitVector), 2);
                valueKeyMap.put(cipherText[i], postXor);
            }
        }

        // now, decode the string
        StringBuilder builder = new StringBuilder();

        String binaryInitVector = Integer.toBinaryString(initVector);
        String binaryPostXor = Integer.toBinaryString(valueKeyMap.get(input[0]));
        int decoded = Integer.parseInt(xor(binaryPostXor, binaryInitVector), 2);
        builder.append((char)decoded);

        for (int i = 1; i < input.length; i++) {
            binaryInitVector = Integer.toBinaryString(input[i-1]);
            binaryPostXor = Integer.toBinaryString(valueKeyMap.get(input[i]));
            decoded = Integer.parseInt(xor(binaryPostXor, binaryInitVector), 2);
            builder.append((char)decoded);
        }

        return builder.toString();
    }

    /* Helper Functions below */

    private String xor(String a, String b) {
        // Lengths of the given strings
        int aLen = a.length();
        int bLen = b.length();

        // Make both the strings of equal lengths
        // by inserting 0s in the beginning
        if (aLen > bLen) {
            b = addZeros(b, aLen - bLen);
        }
        else if (bLen > aLen) {
            a = addZeros(a, bLen - aLen);
        }

        // Updated length
        int len = Math.max(aLen, bLen);

        // To store the resultant XOR
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < len; i++) {
            if (a.charAt(i) == b.charAt(i))
                res.append("0");
            else
                res.append("1");
        }

        return res.toString();
    }

    private String addZeros (String str, int n) {
        StringBuilder strBuilder = new StringBuilder(str);
        for (int i = 0; i < n; i++) {
            strBuilder.insert(0, "0");
        }
        str = strBuilder.toString();

        return str;
    }
}
