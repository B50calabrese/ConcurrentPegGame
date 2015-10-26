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
 * Returns the string representation of the board job.
 */
func BoardJobToString(b BoardJob) string {
    return "(" + strconv.Itoa(b.InitialPeg) + "," + strconv.Itoa(b.TotalMoves) + ")" + b.MoveString
}