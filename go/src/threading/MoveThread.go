package threading

import (
    "container/list"
    "strconv"
    "util"
)

/**
 * Holds the data that a MoveThread will have to execute with.
 */
type MoveThread struct {
    Job util.BoardJob
    /** TODO(acalabrese): ThreadManager?? **/
    NumberOfRows int
    NumberOfTotalPegs int
    ValidMoves *list.List
}

/**
 * Creates a new MoveThread for the given parameters.
 * TODO(acalabrese): Do I need to do the ThreadManager?
 */
func NewMoveThread(job util.BoardJob, numberOfRows int, numberOfTotalPegs int) MoveThread {
    l := list.New()
    return MoveThread{job, numberOfRows, numberOfTotalPegs, l}
}

/**
 * Returns the string representation of a MoveThread.
 */
func MoveThreadToString(moveThread MoveThread) string {
    return "Rows : " + strconv.Itoa(moveThread.NumberOfRows) + ", Number of Total Pegs : " + strconv.Itoa(moveThread.NumberOfTotalPegs)
}