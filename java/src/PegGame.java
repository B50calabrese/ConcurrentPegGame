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

        int totalPegs = PegGameUtils.TOTAL_PEGS_TABLE[rows];
        ThreadManager manager = new ThreadManager(rows, totalPegs);
        int max = PegGameUtils.TOTAL_PEGS_TABLE[(rows / 2) + (rows % 2)];
        for (int i = 0 ; i < max ; i++) {
            // First create the initial board.
            boolean[] newBoard = new boolean[totalPegs];
            for (int j = 0 ; j < totalPegs ; j++) {
                newBoard[j] = (i == j ? false : true);
            }
            manager.queueJob(new BoardJob(totalPegs - 1, i, 1, newBoard, ""));
        }

        try {
            manager.runJobs();
            System.out.println(manager.getBestJob());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
