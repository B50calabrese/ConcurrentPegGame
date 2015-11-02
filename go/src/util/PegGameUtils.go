package util

var TOTAL_PEGS_TABLE = []int {
    0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78, 91, 105, 120, 136, 153,
}

var MAX_ROWS = 12

var TOTAL_PEGS = 0

var NUMBER_OF_ROWS = 0

/**
 * Sets the total number of pegs for the current test.
 */
func SetTotalPegs(pegs int) {
    TOTAL_PEGS = pegs
}

/**
 * Gets the total number of pegs for the current test.
 */
func GetTotalPegs() int {
    return TOTAL_PEGS
}

/**
 * Sets the number of rows for the current test.
 */
func SetNumberOfRows(rows int) {
    NUMBER_OF_ROWS = rows
}

/**
 * Gets the number of rows for the current test.
 */
func GetNumberOfRows() int {
    return NUMBER_OF_ROWS
}

/**
 * Returns the row for the peg number provided.
 */
func GetRow(pegNumber int) int {
    i := 0
    for j := 0 ; TOTAL_PEGS_TABLE[j] <= pegNumber ; j++ {
        i = j
    }
    return i
}

/**
 * Returns the displacement for the given peg number.
 */
func GetDisplacement(pegNumber int) int {
    if (GetRow(pegNumber) < 0) {
        return -1
    }
    return pegNumber - TOTAL_PEGS_TABLE[GetRow(pegNumber)]
}

func GetPegNumber(row int, displacement int) int {
    if (row < 0 || row >= MAX_ROWS || displacement < 0 || displacement > row) {
        return -1
    }
    return TOTAL_PEGS_TABLE[row] + displacement
}
