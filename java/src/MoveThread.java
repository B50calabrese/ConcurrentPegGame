import java.util.ArrayList;
import java.util.List;

/**
 * A thread that will do move computations.
 */
public class MoveThread extends Thread {

    private BoardJob job;
    private ThreadManager threadManager;
    private int numberOfRows;
    private int numberOfTotalPegs;
    private List<Move> validMoves;

    public MoveThread(BoardJob job, ThreadManager threadManager) {
        this.job = job;
        this.threadManager = threadManager;
        this.numberOfRows = threadManager.getNumberOfRows();
        this.numberOfTotalPegs = threadManager.getNumberOfTotalPegs();
        validMoves = new ArrayList<>();
    }

    @Override
    public void run() {
        // First we populate the list of all valid moves by testing the neighbors.
        for (int i = 0 ; i < numberOfTotalPegs ; i++) {
            if (job.board[i]) {
                testNeighborMoves(i);
            }
        }

        // If we found no valid move then we are done!!!
        if (validMoves.size() == 0) {
            threadManager.foundBoard(job);
            threadManager.threadFinished(this);
            return;
        }

        // If we still have moves, then we need to enque the jobs on the thread manager's queue.
        for (Move move : validMoves) {
            boolean[] newBoard = job.board.clone();
            newBoard[move.newPosition] = true;
            newBoard[move.originalPosition] = false;
            newBoard[move.removedPiece] = false;
            BoardJob newJob =
                    new BoardJob(job.pegsLeft - 1, job.initialPeg, job.totalMoves + 1, newBoard,
                            job.moveString + "\n" + move);
            threadManager.queueJob(newJob);
        }

        threadManager.threadFinished(this);
    }

    /**
     * Finds all available moves for the peg in question and then adds them to the valid list.
     */
    private void testNeighborMoves(int pegToInspect) {
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
            boolean b =
                    (job.board[move.originalPosition] && !job.board[move.newPosition] && job.board[move.removedPiece]);
            if (b) {
                validMoves.add(move);
            }
        }
    }
}
