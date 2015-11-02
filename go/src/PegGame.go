package main

import (
    "fmt"
    "os"
    "strconv"
    "util"
    "threading"
)


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
    channel := make(chan string, 1)

    for i := 0 ; i < maxPegs ; i++ {
        newBoard := createBoard(i, totalPegs)
        job := util.BoardJob{totalPegs - 1, i, 0, "", newBoard}
        thread := threading.NewMoveThread(job)
        go threading.RunMoveThread(thread, channel)
    }

    val := <-channel
    fmt.Println(val)
}

/**
 * Creates a fresh board with a peg removed.
 */
func createBoard(falsePeg int, totalPegs int) []bool {
    newBoard := make([]bool, totalPegs, totalPegs + 1)
    for j := 0 ; j < totalPegs ; j++ {
        if (falsePeg == j) {
            newBoard[j] = false
        } else {
            newBoard[j] = true
        }
    }
    return newBoard
}
