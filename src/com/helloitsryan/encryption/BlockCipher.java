package com.helloitsryan.encryption;

import java.util.Arrays;

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
        System.out.println(Arrays.toString(
                blockCipher.encryptCipherBlockChaining(plaintext, 23)
        ));
    }

    private final int[] key;

    public BlockCipher(final int[] key) {
        this.key = key;
        System.out.println(key.length);
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
