package util

import "strconv"

/**
 * Move struct for holding the data associated with a singular move.
 */
type Move struct {
    OriginalPosition int
    NewPosition int
    RemovePiece int
}

/**
 * Returns the string representation of a move.
 */
func MoveToString(m Move) string {
    return "(" + strconv.Itoa(m.OriginalPosition) + "," + strconv.Itoa(m.NewPosition) + ")"
}