package util

import "strconv"

/**
 * Represents a unit of work that can be worked upon.
 */
type BoardJob struct {
    PegsLeft int
    InitialPeg int
    TotalMoves int
    MoveString string
    Board []bool
}

/**
 * Creates the next board job given the previous one and the new state of the board.
 */
func NewBoardJobWithMove(job BoardJob, newBoard []bool, moveString string) BoardJob {
    pegsLeft := job.PegsLeft
    initialPeg := job.InitialPeg
    totalMoves := job.TotalMoves + 1
    newMoveString := job.MoveString + "\n" + moveString
    return BoardJob{pegsLeft, initialPeg, totalMoves, newMoveString, newBoard}
}

/**
 * Returns the string representation of the board job.
 */
func BoardJobToString(b BoardJob) string {
    return "(" + strconv.Itoa(b.InitialPeg) + "," + strconv.Itoa(b.TotalMoves) + ")" + b.MoveString
}