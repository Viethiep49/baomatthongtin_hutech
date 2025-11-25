package week_01;

public class AutokeyCipher {
    public static String encrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();

        // Construct the key stream
        StringBuilder keyStream = new StringBuilder(key);
        keyStream.append(text);
        // The key stream only needs to be as long as the text
        // Actually, for Autokey, the key is the primer + the plaintext itself.
        // So for char i, the shift is determined by keyStream.charAt(i).

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                int shift = keyStream.charAt(i) - 'A';
                char encrypted = (char) ((c - 'A' + shift) % 26 + 'A');
                result.append(encrypted);
            } else {
                result.append(c);
                // If we skip non-letters, we should probably not consume the key stream?
                // Standard Vigenere/Autokey usually skips non-letters but keeps key index or
                // not?
                // Let's assume we just append non-letters and don't shift,
                // but for Autokey, the key stream is usually based on the *plaintext letters*.
                // So if we have a space, we just output space.
                // But does the key stream advance?
                // If I have "HELLO WORLD", key "K".
                // H encrypted with K.
                // E encrypted with H.
                // L encrypted with E.
                // L encrypted with L.
                // O encrypted with L.
                // Space.
                // W encrypted with O.
                // So we need to track the index in the key stream separately from the text
                // index if we skip chars.
                // But for simplicity here, let's assume we only process letters and build the
                // key stream from letters only.
            }
        }
        return result.toString();
    }

    // Let's refine the logic to be robust.
    // We will build the keystream dynamically or just use indices.
    // Better:

    public static String encryptRobust(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();

        StringBuilder currentKey = new StringBuilder(key);

        int keyIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                // Get shift from current key char
                char k = currentKey.charAt(keyIndex);
                int shift = k - 'A';
                char encrypted = (char) ((c - 'A' + shift) % 26 + 'A');
                result.append(encrypted);

                // Add the PLAINTEXT char to the key stream for future use
                currentKey.append(c);
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();

        StringBuilder currentKey = new StringBuilder(key);
        int keyIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                char k = currentKey.charAt(keyIndex);
                int shift = k - 'A';
                // Decrypt: (C - K + 26) % 26
                char decrypted = (char) ((c - 'A' - shift + 26) % 26 + 'A');
                result.append(decrypted);

                // Add the DECRYPTED char to the key stream
                currentKey.append(decrypted);
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
