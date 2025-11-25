package week_01;

public class RailFenceCipher {
    public static String encrypt(String text, int key) {
        if (key < 2)
            return text;
        char[][] rail = new char[key][text.length()];
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                rail[i][j] = '\n';
            }
        }
        boolean dirDown = false;
        int row = 0, col = 0;
        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1)
                dirDown = !dirDown;
            rail[row][col++] = text.charAt(i);
            if (dirDown)
                row++;
            else
                row--;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] != '\n')
                    result.append(rail[i][j]);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, int key) {
        if (key < 2)
            return text;
        char[][] rail = new char[key][text.length()];
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                rail[i][j] = '\n';
            }
        }
        boolean dirDown = false;
        int row = 0, col = 0;
        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1)
                dirDown = !dirDown;
            rail[row][col++] = '*';
            if (dirDown)
                row++;
            else
                row--;
        }
        int index = 0;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] == '*' && index < text.length()) {
                    rail[i][j] = text.charAt(index++);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        row = 0;
        col = 0;
        dirDown = false;
        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1)
                dirDown = !dirDown;
            if (rail[row][col] != '\n')
                result.append(rail[row][col++]);
            if (dirDown)
                row++;
            else
                row--;
        }
        return result.toString();
    }
}
