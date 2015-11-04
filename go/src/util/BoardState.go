package util

import (
    "container/list"
    "strconv"
)

/**
 * Structure that represents the current state of a board.
 */
type BoardState struct {
    Board []bool
    PegsLeft int
    PreviousMoves *list.List
    BestMoveString string
    InitialPeg int
    BestLeft int
}

/**
 * Creates a new BoardState structure with the given params.
 */
func NewBoardState(board []bool, pegsLeft int, initialPeg int) *BoardState {
    l := list.New()
    return &(BoardState{board, pegsLeft, l, "", initialPeg, 0})
}

/**
 * Gets the string representation of the move list.
 */
func GetBoardMoveString(state *BoardState) string {
    element := state.PreviousMoves.Front()
    str := "(" + strconv.Itoa(state.InitialPeg) + "," + strconv.Itoa(GetTotalPegs() - state.PreviousMoves.Len() - 1 )+ ")"
    for i := 0 ; i < state.PreviousMoves.Len() ; i = i + 1 {
        move := element.Value.(Move)
        str = str + "\n" + MoveToString(move)
        element = element.Next()
    }
    return str
} 

/**
 * Applies a move to the BoardState given, keeping track of various things.
 */
func ApplyMove(boardState *BoardState, move Move) {
    boardState.Board[move.OriginalPosition] = false
    boardState.Board[move.NewPosition] = true
    boardState.Board[move.RemovePiece] = false
    boardState.PegsLeft = boardState.PegsLeft - 1
    boardState.PreviousMoves.PushBack(move)
}

/**
 * Undos the last move on the BoardState.
 */
func UndoLastMove(boardState *BoardState) {
    element := boardState.PreviousMoves.Back() 
    move := element.Value.(Move)
    boardState.Board[move.OriginalPosition] = true
    boardState.Board[move.NewPosition] = false
    boardState.Board[move.RemovePiece] = true
    boardState.PegsLeft = boardState.PegsLeft + 1
    boardState.PreviousMoves.Remove(element)
}
    
