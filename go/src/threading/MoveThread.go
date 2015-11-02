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
    ValidMoves *list.List
}

/**
 * Creates a new MoveThread for the given parameters.
 */
func NewMoveThread(job util.BoardJob) MoveThread {
    l := list.New()
    return MoveThread{job, l}
}

/**
 * Returns the string representation of a MoveThread.
 */
func MoveThreadToString(moveThread MoveThread) string {
    return "Rows : " + strconv.Itoa(util.GetNumberOfRows()) + ", Number of Total Pegs : " + strconv.Itoa(util.GetTotalPegs())
}

func RunMoveThread(moveThread MoveThread, channel chan string) {
    // First populate the move thread's list of moves.
    for i := 0 ; i < util.GetTotalPegs() ; i++ {
        if (moveThread.Job.Board[i]) {
            testNeighborMoves(moveThread, i)
        }
    }

    // If there a no valid moves then we need to notify somebody.
    if (moveThread.ValidMoves.Len() == 0) {
        channel <- util.BoardJobToString(moveThread.Job)
        return;
    }

    // If there are still moves then we will add the jobs to the job queue.
    element := moveThread.ValidMoves.Front()
    for i := 0 ; i < moveThread.ValidMoves.Len() ; i++ {
        move := element.Value.(util.Move)
        newBoard := cloneBoard(moveThread)
        newBoard[move.NewPosition] = true
        newBoard[move.OriginalPosition] = false
        newBoard[move.RemovePiece] = false
        newJob := util.NewBoardJobWithMove(moveThread.Job, newBoard, util.MoveToString(move))
        newMoveThread := NewMoveThread(newJob)
        go RunMoveThread(newMoveThread, channel)
        element = element.Next()
    }
}

/*******************
 * Private Methods *
 *******************/
func cloneBoard(moveThread MoveThread) []bool {
    newBoard := make([]bool, util.GetTotalPegs())
    for i := 0 ; i < util.GetTotalPegs() ; i++ {
        if (moveThread.Job.Board[i]) {
            newBoard[i] = true
        } else {
            newBoard[i] = false
        }
    }
    return newBoard
}


/**
 * Finds all available moves for a peg in question and adds it to the set of valid moves.
 */
func testNeighborMoves(moveThread MoveThread, pegToInspect int) {
    r := util.GetRow(pegToInspect)
    d := util.GetDisplacement(pegToInspect)

    /**
     * There are only 6 possible directions the peg can jump based on the diagram below.
     *
     *  6 1
     * 5 0 2
     *  4 3
     */

    // Direction 1
    land := util.GetPegNumber(r - 2, d)
    jump := util.GetPegNumber(r - 1, d)
    testMove(moveThread, util.Move{pegToInspect, land, jump})

    // Direction 2
    land = util.GetPegNumber(r, d + 2)
    jump = util.GetPegNumber(r, d + 1)
    testMove(moveThread, util.Move{pegToInspect, land, jump})

    // Direction 3
    land = util.GetPegNumber(r + 2, d + 2)
    jump = util.GetPegNumber(r + 1, d + 1)
    testMove(moveThread, util.Move{pegToInspect, land, jump})

    // Direction 4
    land = util.GetPegNumber(r + 2, d)
    jump = util.GetPegNumber(r + 1, d)
    testMove(moveThread, util.Move{pegToInspect, land, jump})

    // Direction 5
    land = util.GetPegNumber(r, d - 2)
    jump = util.GetPegNumber(r, d - 1)
    testMove(moveThread, util.Move{pegToInspect, land, jump})

    // Direction 6
    land = util.GetPegNumber(r - 2, d - 2)
    jump = util.GetPegNumber(r - 1, d - 1)
    testMove(moveThread, util.Move{pegToInspect, land, jump})
}

/**
  * Tests a move to see if it is valid, and if so it adds the move to the valid list.
  */
func testMove(moveThread MoveThread, move util.Move) {
    lR := util.GetRow(move.NewPosition)
    if (lR < 0 || lR >= util.GetNumberOfRows()) {
        return;
    }
    lD := util.GetDisplacement(move.NewPosition)
    if (0 <= lR && lR <= util.GetNumberOfRows() && 0 <= lD && lD <= lR) {
        b := (moveThread.Job.Board[move.OriginalPosition] && !moveThread.Job.Board[move.NewPosition] && moveThread.Job.Board[move.RemovePiece])
        if (b) {
            moveThread.ValidMoves.PushFront(move)
        }
    }
}
