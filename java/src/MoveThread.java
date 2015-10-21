import java.util.ArrayList;
import java.util.List;

/**
 * A thread that will do move computations.
 */
public class MoveThread extends Thread {

    private boolean[] board;
    private int pegToInspect;
    private int numberOfRows;
    private List<Move> validMoves;

    public MoveThread(int peg, boolean[] board, int numberOfRows) {
        this.numberOfRows = numberOfRows;
        pegToInspect = peg;
        this.board = board;
        validMoves = new ArrayList<>();
    }

    public List<Move> getValidMoves() {
        return validMoves;
    }

    @Override
    public void run() {
        // These are the 6 possible move directions with O being the peg in
        // question.
        //  6 1
        // 5 O 2
        //  4 3
        // We will test these in order and compile a list of the moves that are valid that the parent thread can get
        // after joining.

        int r = PegGameUtils.getRow(pegToInspect);
        int d = PegGameUtils.getDisplacement(pegToInspect);
        int land;
        int jump;

        // logger.info("Original : " + pegToInspect);

        // 1
        // ((R, D), (R - 2, D), (R - 1, D))
        land = PegGameUtils.getPegNumber(r - 2, d);
        jump = PegGameUtils.getPegNumber(r - 1, d);
        // logger.info("Test before land = " + land + " and jump = " + jump);
        testMove(new Move(pegToInspect, land, jump));

        // 2
        // ((R, D), (R, D + 2), (R, D + 1))
        land = PegGameUtils.getPegNumber(r, d + 2);
        jump = PegGameUtils.getPegNumber(r, d + 1);
        // logger.info("Test before land = " + land + " and jump = " + jump);
        testMove(new Move(pegToInspect, land, jump));

        // 3
        // ((R, D), (R + 2, D + 2), (R + 1, D + 1))
        land = PegGameUtils.getPegNumber(r + 2, d + 2);
        jump = PegGameUtils.getPegNumber(r + 1, d + 1);
        // logger.info("Test before land = " + land + " and jump = " + jump);
        testMove(new Move(pegToInspect, land, jump));

        // 4
        // ((R, D), (R - 2, D - 2), (R - 1, D - 1))
        land = PegGameUtils.getPegNumber(r + 2, d);
        jump = PegGameUtils.getPegNumber(r + 1, d);
        // logger.info("Test before land = " + land + " and jump = " + jump);
        testMove(new Move(pegToInspect, land, jump));

        // 5
        // ((R, D), (R, D - 2), (R, D - 1))
        land = PegGameUtils.getPegNumber(r, d - 2);
        jump = PegGameUtils.getPegNumber(r, d - 1);
        // logger.info("Test before land = " + land + " and jump = " + jump);
        testMove(new Move(pegToInspect, land, jump));

        // 6
        // ((R, D), (R - 2, D - 2), (R - 1, D - 1))
        land = PegGameUtils.getPegNumber(r - 2, d - 2);
        jump = PegGameUtils.getPegNumber(r - 1, d - 1);
        // logger.info("Test before land = " + land + " and jump = " + jump);
        testMove(new Move(pegToInspect, land, jump));
    }

    /**
     * Tests the move and adds it to the list of valid moves if the move is valid.
     */
    private void testMove(Move move) {
        int lR = PegGameUtils.getRow(move.newPosition);
        if (lR < 0 || lR >= numberOfRows) {
            return;
        }
        int lD = PegGameUtils.getDisplacement(move.newPosition);
        if (0 <= lR && lR <= numberOfRows - 1 && 0 <= lD && lD <= lR) {
            boolean b = (board[move.originalPosition] && !board[move.newPosition] && board[move.removedPiece]);
            if (b) {
                validMoves.add(move);
            }
        }
    }
}
