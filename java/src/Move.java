/**
 * Represents a move in the game board.
 */
public class Move {
    public int originalPosition;
    public int newPosition;
    public int removedPiece;

    public Move(int originalPosition, int newPosition, int removedPiece) {
        this.originalPosition = originalPosition;
        this.newPosition = newPosition;
        this.removedPiece = removedPiece;
    }

    public String toString() {
        return "(" + originalPosition + "," + newPosition + ")";
    }

    public boolean isEqual(Move move) {
        if (    move.originalPosition == this.originalPosition &&
                move.newPosition == this.newPosition &&
                move.removedPiece == this.removedPiece) {
            return true;
        }
        return false;
    }
}
