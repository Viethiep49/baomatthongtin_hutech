package week_01;

import java.util.Arrays;

public class TranspositionCipher {
    public static String encrypt(String text, String key) {
        int[] keyArr = getKeyOrder(key);
        int col = key.length();
        int row = (int) Math.ceil((double) text.length() / col);
        char[][] grid = new char[row][col];
        int k = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (k < text.length()) {
                    grid[i][j] = text.charAt(k++);
                } else {
                    grid[i][j] = '\0'; // Padding
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < col; i++) {
            int currentKeyIndex = -1;
            // Find which column is next based on key order
            for (int idx = 0; idx < keyArr.length; idx++) {
                if (keyArr[idx] == i) {
                    currentKeyIndex = idx;
                    break;
                }
            }

            for (int j = 0; j < row; j++) {
                if (grid[j][currentKeyIndex] != '\0') {
                    result.append(grid[j][currentKeyIndex]);
                }
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, String key) {
        int[] keyArr = getKeyOrder(key);
        int col = key.length();
        int row = (int) Math.ceil((double) text.length() / col);
        char[][] grid = new char[row][col];

        // Calculate number of characters in each column
        int[] colLengths = new int[col];
        int fullCells = text.length();
        int fullRows = fullCells / col;
        int extraCells = fullCells % col;

        for (int i = 0; i < col; i++) {
            colLengths[i] = fullRows + (i < extraCells ? 1 : 0);
        }

        // Fill grid column by column based on key order
        int currentTextIdx = 0;
        for (int i = 0; i < col; i++) {
            int currentKeyIndex = -1;
            // Find which column is next based on key order
            for (int idx = 0; idx < keyArr.length; idx++) {
                if (keyArr[idx] == i) {
                    currentKeyIndex = idx;
                    break;
                }
            }

            // Adjust for the fact that in the grid, the last row might not be full
            // The logic above for colLengths assumes filling left to right.
            // But we read out based on key order.
            // So we need to know how many chars are in the column *corresponding to the key
            // index*.
            // Actually, the standard columnar fills row by row, so the last row is filled
            // left to right.
            // So columns 0 to extraCells-1 have fullRows+1, others have fullRows.

            int charsInThisCol = fullRows + (currentKeyIndex < extraCells ? 1 : 0);

            for (int j = 0; j < charsInThisCol; j++) {
                if (currentTextIdx < text.length()) {
                    grid[j][currentKeyIndex] = text.charAt(currentTextIdx++);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] != '\0') {
                    result.append(grid[i][j]);
                }
            }
        }
        return result.toString();
    }

    private static int[] getKeyOrder(String key) {
        int[] order = new int[key.length()];
        Character[] chars = new Character[key.length()];
        for (int i = 0; i < key.length(); i++) {
            chars[i] = key.charAt(i);
        }
        Character[] sortedChars = chars.clone();
        Arrays.sort(sortedChars);

        boolean[] used = new boolean[key.length()];
        for (int i = 0; i < key.length(); i++) {
            for (int j = 0; j < key.length(); j++) {
                if (chars[i] == sortedChars[j] && !used[j]) {
                    order[i] = j; // This char is the j-th in sorted order
                    used[j] = true;
                    break;
                }
            }
        }
        // Wait, standard implementation:
        // Key: ZEBRAS (632415) -> columns are read in order 1, 2, 3, 4, 5, 6
        // So we need to know: which column index corresponds to '1', which to '2', etc.
        // My getOrder returns: for each char in key, what is its rank.
        // ZEBRAS -> Z(5), E(2), B(1), R(4), A(0), S(3) (0-indexed)
        // So order array is [5, 2, 1, 4, 0, 3]
        // Encrypt loop: i from 0 to col-1. Find index in order array where value is i.
        // i=0 -> value 0 is at index 4 (A). Read column 4.
        // i=1 -> value 1 is at index 2 (B). Read column 2.
        // Correct.
        return order;
    }
}
