import java.util.ArrayList;
import java.util.List;

/**
 * A thread style class that will deal with solving a particular board.
 */
public class PegThread extends Thread {

    private boolean[] board;
    private int numberOfPegsLeft;
    private PegGameThreadManager manager;
    private int bestScore;

    public PegThread(boolean[] board, int numberOfPegsLeft, PegGameThreadManager manager) {
        this.board = board;
        this.numberOfPegsLeft = numberOfPegsLeft;
        this.manager = manager;
        bestScore = 0;
    }

    /**
     * @returns the best score achieved for this thread and its children.
     */
    public int getBestScore() {
        return bestScore;
    }

    @Override
    public void run() {
        // First check that we are not below the best score in order to limit the amount of computation.
        if (numberOfPegsLeft <= manager.getCurrentBestScore()) {
            return;
        }

        // Next we need to find all valid moves.
        List<Move> validMoves = getValidMoves();

        // If the size of the list of valid moves is zero, meaning there is no move, then this is the end point to go
        // back without spawning new threads.
        if (validMoves.size() == 0) {
            bestScore = numberOfPegsLeft;
            return;
        }

        // Once we have this we need to spawn threads for each move, making a new board for each, and then join on them.
        spawnChildrenAndWait(validMoves);

        manager.setCurrentBestScore(bestScore);
    }

    /**
     * @returns a list of the valid moves given the current board.
     */
    private List<Move> getValidMoves() {
        List<Move> moves = new ArrayList<>();
        List<MoveThread> moveThreads = new ArrayList<>();
        for (int i = 0 ; i < 15 ; i++) {
            if (board[i]) {
                MoveThread thread = new MoveThread(i, board, manager.getNumberOfRows());
                moveThreads.add(thread);
                thread.start();
            }
        }

        for (int i = 0 ; i < moveThreads.size() ; i++) {
            try {
                moveThreads.get(i).join();
                moves.addAll(moveThreads.get(i).getValidMoves());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return moves;
    }

    private void spawnChildrenAndWait(List<Move> validMoves) {
        List<PegThread> threads = new ArrayList<>();
        for (Move m : validMoves) {
            // Create the new board.
            boolean[] newBoard = board.clone();
            newBoard[m.originalPosition] = false;
            newBoard[m.newPosition] = true;
            newBoard[m.removedPiece] = false;

            PegThread pegThread = new PegThread(newBoard, numberOfPegsLeft - 1, manager);
            threads.add(pegThread);
            pegThread.start();
        }

        // Wait until the children threads finish, and get the best amongst them.
        for (PegThread thread : threads) {
            try {
                thread.join();
                bestScore = (bestScore < thread.getBestScore() ? thread.getBestScore() : bestScore);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
