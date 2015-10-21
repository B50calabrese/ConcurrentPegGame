import java.util.ArrayList;
import java.util.List;

/**
 * Main driver class for the PegGame.
 */
public class PegGame {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error: Incorrect amount of arguments.");
            System.exit(0);
        }

        int rows = 0;
        try {
            rows = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Expected -s arg to be an integer.");
            System.exit(0);
        }

        if (!(args[0].equals("-s")) || (rows < 5) || (rows > 10)) {
            System.out.println("Error: Invalid arguments.");
            System.exit(0);
        }

        // TODO(acalabrese): Solve here.
        PegGameThreadManager manager = new PegGameThreadManager(15, 5);

        List<PegThread> initialThreads = new ArrayList<>();

        for (int i = 0 ; i < manager.getNumberOfPegs() ; i++) {
            // First create the initial board.
            boolean[] newBoard = new boolean[manager.getNumberOfPegs()];
            for (int j = 0 ; j < manager.getNumberOfPegs() ; j++) {
                newBoard[j] = (i == j ? false : true);
            }

            PegThread pegThread = new PegThread(newBoard, manager.getNumberOfPegs() - 1, manager);
            initialThreads.add(pegThread);
            pegThread.start();
        }

        for (PegThread thread : initialThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Best score was : " + manager.getCurrentBestScore());
    }
}
