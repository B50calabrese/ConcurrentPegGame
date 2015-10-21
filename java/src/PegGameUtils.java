/**
 * Utility class with some basic methods used across the entire program.
 */
public class PegGameUtils {
    // This table contains the total number of pegs up to a certain row.
    public static final int[] TOTAL_PEGS_TABLE = {
            0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78, 91, 105, 120, 136, 153
    };

    /**
     * Returns the corresponding row for the given peg.
     */
    public static int getRow(int pegNumber) {
        int i;
        // Loop until the number of pegs becomes greater.
        for (i = 0 ; TOTAL_PEGS_TABLE[i] <= pegNumber ; i++);
        return i - 1;
    }

    /**
     * Returns the displacement from the beginning of the row.
     */
    public static int getDisplacement(int pegNumber) {
        return pegNumber - TOTAL_PEGS_TABLE[getRow(pegNumber)];
    }

    /**
     * Returns the peg number for the given row and displacement.
     */
    public static int getPegNumber(int row, int displacement) {
        if (row < 0 || row >= TOTAL_PEGS_TABLE.length || displacement < 0 ||
                displacement > row) {
            return -1;
        }
        return TOTAL_PEGS_TABLE[row] + displacement;
    }
}
