/**
 * Represents a "job" that needs to be done on a thread.
 */
public class BoardJob implements Comparable {
    public int pegsLeft;
    public int initialPeg;
    public int totalMoves;
    public boolean[] board;
    public String moveString;

    public BoardJob(int pegsLeft, int initialPeg, int totalMoves, boolean[] board, String moveString) {
        this.pegsLeft = pegsLeft;
        this.initialPeg = initialPeg;
        this.totalMoves = totalMoves;
        this.board = board;
        this.moveString = moveString;
    }

    @Override
    public int compareTo(Object o) {
        if (this.pegsLeft > ((BoardJob) o).pegsLeft) {
            return -1;
        } else if (this.pegsLeft < ((BoardJob) o).pegsLeft){
            return 1;
        }
        if (this.initialPeg > ((BoardJob) o).initialPeg) {
            return -1;
        } else if (this.initialPeg < ((BoardJob) o).initialPeg){
            return 1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return "(" + initialPeg + "," + totalMoves + ")" + moveString;
    }
}
