package net.aflb.kaas.utils;

import java.util.Arrays;

public class ArrayUtils {

    private ArrayUtils() {}

    // TODO figure out a way to make the copy functions generic (or use collections instead)

    public static int[][][] copy3(final int[][][] original) {
        final var iLength = original.length;
        final var copy = new int[iLength][][];
        for (int i = 0; i < iLength; i++) {
            final var jLength = original[i].length;
            copy[i] = new int[jLength][];
            for (int j = 0; j < jLength; j++) {
                copy[i][j] = Arrays.copyOf(original[i][j], original[i][j].length);
            }
        }

        return copy;
    }

    public static String[][] copy2(final String[][] original) {
        final var iLength = original.length;
        final var copy = new String[iLength][];
        for (int i = 0; i < iLength; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }

        return copy;
    }
}
