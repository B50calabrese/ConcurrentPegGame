import java.util.*;

/**
 * Manages the pool of threads that can be used to compute some work.
 */
public class ThreadManager {
    public static final int MAX_THREADS = 1000;

    private boolean finished;
    private List<Thread> threadPool;
    private List<BoardJob>[] jobArray;
    private int queuedJobs;
    private BoardJob bestJob;
    private int numberOfRows;
    private int numberOfTotalPegs;
    private Object lock;

    public ThreadManager(int numberOfRows, int numberOfTotalPegs) {
        this.numberOfRows = numberOfRows;
        this.numberOfTotalPegs = numberOfTotalPegs;
        threadPool = new ArrayList<>();
        lock = new Object();
        jobArray = new List[numberOfTotalPegs];
        for (int i = 0 ; i < numberOfTotalPegs ; i++) {
            jobArray[i] = new ArrayList<>();
        }
        queuedJobs = 0;
    }

    public int getNumberOfTotalPegs() {
        return numberOfTotalPegs;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public BoardJob getBestJob() {
        return bestJob;
    }

    /**
     * Blocks on the queue and adds the job to the queue.
     */
    public void queueJob(BoardJob job) {
        synchronized (lock) {
            jobArray[job.pegsLeft].add(job);
            queuedJobs++;
            lock.notifyAll();
        }
    }

    /**
     * Removes the thread from the thread pool when it finishes.
     */
    public void threadFinished(Thread t) {
        synchronized (lock) {
            threadPool.remove(t);
            lock.notifyAll();
        }
    }

    /**
     * Used to terminate the search for a board, passing the job that was successful.
     */
    public void foundBoard(BoardJob job) {
        finished = true;
        bestJob = job;
    }

    /**
     * Executes the jobs on the job queue.
     */
    public void runJobs() throws InterruptedException {
        while (!finished) {
            synchronized (lock) {
                // If we have no jobs, wait until we have work to do.
                while (queuedJobs <= 0) {
                    lock.wait();
                }

                // Once finished we can wait for a thread to open up, and from there we can execute the job on that
                // thread.

                while (threadPool.size() >= MAX_THREADS) {
                    lock.wait();
                }

                BoardJob job = getNextJob();
                queuedJobs--;
                Thread t = new MoveThread(job, this);
                threadPool.add(t);
                t.start();
            }
        }
    }

    /**
     * Gets the next job that comes in the array of lists.
     */
    private BoardJob getNextJob() {
        for (int i = numberOfTotalPegs - 1 ; i >= 0 ; i--) {
            if (jobArray[i].size() > 0) {
                return jobArray[i].remove(0);
            }
        }
        return null;
    }
}
