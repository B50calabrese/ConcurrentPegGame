package main

import (
    "fmt"
    "os"
    "strconv"
    "util"
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
    
    fmt.Println("Rows : ", util.TOTAL_PEGS_TABLE[rows]);
    fmt.Println("Row : ", util.GetRow(4));
    fmt.Println("Displacement : ", util.GetDisplacement(4));
    fmt.Println("PegNumber : ", util.GetPegNumber(2, 1));
}