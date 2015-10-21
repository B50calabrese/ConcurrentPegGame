/**
 * Manages the pool of threads and the updating of the best score.
 */
public class PegGameThreadManager {

    private int currentBestScore;
    private int numberOfPegs;
    private int numberOfRows;

    public PegGameThreadManager(int numberOfPegs, int numberOfRows) {
        this.numberOfPegs = numberOfPegs;
        this.numberOfRows = numberOfRows;
        currentBestScore = 0;
    }

    /**
     * @returns the number of rows in the board.
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * @returns the number of pegs in the board.
     */
    public int getNumberOfPegs() {
        return numberOfPegs;
    }

    /**
     * @returns the current best score achieved.
     */
    public int getCurrentBestScore() {
        return currentBestScore;
    }

    /**
     * Sets the best score if it is truly better. Note this is a synchronized method meaning that only one call to this
     * can occur at a time.
     */
    public synchronized void setCurrentBestScore(int best) {
        currentBestScore = (best > currentBestScore ? best : currentBestScore);
    }
}
