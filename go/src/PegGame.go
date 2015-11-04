package main

import (
    "container/list"
    "fmt"
    "os"
    "strconv"
    "util"
)

var bestScore = 0
var bestString = ""

/**
 * Main entry point for the code.
 */
func main() {
    argsWithoutProg := os.Args[1:]

    if (argsWithoutProg[0] != "-s") {
        fmt.Println("Need to provide -s")
        os.Exit(0)
    }

    rows, err := strconv.Atoi(argsWithoutProg[1])

    if (err != nil) {
        fmt.Println(err)
        os.Exit(0)
    }

    totalPegs := util.TOTAL_PEGS_TABLE[rows]

    // Some bookkeeping to have a central location for useful data.
    util.SetTotalPegs(totalPegs)
    util.SetNumberOfRows(rows)

    maxPegs := util.TOTAL_PEGS_TABLE[(rows / 2) + (rows % 2)]
    
    // Create a channel to sync up all threads.
    returnChannel := make(chan int, maxPegs)

    // Create channel to sync best boards from all threads.
    updateChannel := createUpdateFunction()

    // Spawn off the recursive solves.
    for i := 0 ; i < maxPegs ; i = i + 1 {
        state := createBoardState(i, totalPegs)
        go solveBoardState(state, returnChannel, updateChannel)
    }

    // Wait for all threads to finish.
    for i := 0 ; i < maxPegs ; i = i + 1 {
        <- returnChannel
    }
    
    fmt.Println(bestString)
}

/**
 * Spawns thread that listens for updates on the best board, returning the channel to communicate with it.
 */
func createUpdateFunction() chan *util.BoardState {
    updateChannel := make(chan *util.BoardState)
    go func() {
        for {
            state := <- updateChannel
            if (state.PegsLeft > bestScore) {
                bestScore = state.PegsLeft
                bestString = util.GetBoardMoveString(state)
            }
        }
    } ()
    return updateChannel
}

/**
 * Creates a fresh board state with a peg removed.
 */
func createBoardState(falsePeg int, totalPegs int) *util.BoardState {
    newBoard := make([]bool, totalPegs, totalPegs + 1)
    for j := 0 ; j < totalPegs ; j++ {
        if (falsePeg == j) {
            newBoard[j] = false
        } else {
            newBoard[j] = true
        }
    }
    return util.NewBoardState(newBoard, totalPegs - 1, falsePeg)
}

/**
 * Solves for the best board state possible.
 */
func solveBoardState(state *util.BoardState, syncChannel chan int, updateChannel chan *util.BoardState) { 
    recursiveSolve(state, updateChannel)
    syncChannel <- 1        
}

/**
 * Recursively solves a board state.
 */
func recursiveSolve(state *util.BoardState, updateChannel chan *util.BoardState) {
    if (state.PegsLeft > bestScore) {
        validMovesList := list.New()
        
        // We need to compile the list of all possible valid moves.
        for i := 0 ; i < util.GetTotalPegs() ; i = i + 1 {
            // If there is a peg, we will add all possible valid moves to our list.
            if (state.Board[i]) {
                validMovesList.PushBackList(getValidMovesForPosition(state, i))
            }
        }

        // If this is truly our best, then we will save it.
        if (validMovesList.Len() == 0) {
            updateChannel <- state
        } else {
            // If there are moves, let us go through them all and apply them.
            element := validMovesList.Front()
            for i := 0 ; i < validMovesList.Len() ; i++ {
                move := element.Value.(util.Move)
                util.ApplyMove(state, move)
                recursiveSolve(state, updateChannel)
                util.UndoLastMove(state)
                element = element.Next()
            } 
        }
    }
}

/**
 * Gets the list of all available moves of the given peg.
 */
func getValidMovesForPosition(state *util.BoardState, peg int) *list.List {
    validMoves := list.New()
    r := util.GetRow(peg)
    d := util.GetDisplacement(peg)

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
    validMoves.PushBackList(testMove(state, util.Move{peg, land, jump}))

    // Direction 2 
    land = util.GetPegNumber(r, d + 2)
    jump = util.GetPegNumber(r, d + 1)
    validMoves.PushBackList(testMove(state, util.Move{peg, land, jump}))

    // Direction 3
    land = util.GetPegNumber(r + 2, d + 2)
    jump = util.GetPegNumber(r + 1, d + 1)
    validMoves.PushBackList(testMove(state, util.Move{peg, land, jump}))

    // Direction 4
    land = util.GetPegNumber(r + 2, d)
    jump = util.GetPegNumber(r + 1, d)
    validMoves.PushBackList(testMove(state, util.Move{peg, land, jump}))

    // Direction 5
    land = util.GetPegNumber(r, d - 2)
    jump = util.GetPegNumber(r, d - 1)
    validMoves.PushBackList(testMove(state, util.Move{peg, land, jump}))

    // Direction 6
    land = util.GetPegNumber(r - 2, d - 2)
    jump = util.GetPegNumber(r - 1, d - 1)
    validMoves.PushBackList(testMove(state, util.Move{peg, land, jump}))

    return validMoves
}

/**
 * Tests a move and returns a list containing the move.
 */
func testMove(state *util.BoardState, move util.Move) *list.List {
    l := list.New()
    lR := util.GetRow(move.NewPosition)
    if (lR <0 || lR >= util.GetNumberOfRows()) {
        return l
    }
    lD := util.GetDisplacement(move.NewPosition)
    if (0 <= lR && lR <= util.GetNumberOfRows() && 0 <= lD && lD <= lR) {
        b := state.Board[move.OriginalPosition] && !state.Board[move.NewPosition] && state.Board[move.RemovePiece]
        if (b) {
            l.PushFront(move)
        }
    }
    return l
}
