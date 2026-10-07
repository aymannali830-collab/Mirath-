package com.example.engine

/**
 * Generic Game Engine Base Interface
 * Decouples game logic from UI rendering.
 */
interface GameEngine<State, Move> {
    fun getInitialState(): State
    fun validateMove(state: State, move: Move): Boolean
    fun getLegalMoves(state: State): List<Move>
    fun applyMove(state: State, move: Move): State
    fun getNextTurn(state: State): Int
    fun isGameOver(state: State): Boolean
    fun getWinner(state: State): Int? // null if ongoing or draw
    fun isDraw(state: State): Boolean
    fun restart(): State = getInitialState()
}

// -------------------------------------------------------------
// 1. Alignment Engine (e.g., Alquerque / Three Men's Morris / Yote / Shisima)
// -------------------------------------------------------------
enum class AlignmentPlayer {
    NONE,
    PLAYER_ONE, // Terracotta pieces
    PLAYER_TWO  // Cream/Gold pieces
}

data class AlignmentState(
    val board: List<AlignmentPlayer>, // 3x3 for Morris/Shisima, or 5x5 for Alquerque
    val size: Int = 3,
    val currentTurn: AlignmentPlayer = AlignmentPlayer.PLAYER_ONE,
    val phase: Int = 1, // 1: drop/placement, 2: movement
    val piecesPlacedP1: Int = 0,
    val piecesPlacedP2: Int = 0,
    val maxPiecesPerPlayer: Int = 3,
    val winner: AlignmentPlayer? = null,
    val isDrawGame: Boolean = false,
    val moveCount: Int = 0,
    val selectedIndex: Int? = null
)

sealed class AlignmentMove {
    data class PlacePiece(val index: Int) : AlignmentMove()
    data class MovePiece(val fromIndex: Int, val toIndex: Int) : AlignmentMove()
    data class SelectPiece(val index: Int?) : AlignmentMove()
}

class AlignmentEngine(
    private val boardDimension: Int = 3,
    private val piecesToPlace: Int = 3
) : GameEngine<AlignmentState, AlignmentMove> {

    override fun getInitialState(): AlignmentState {
        val totalCells = boardDimension * boardDimension
        return AlignmentState(
            board = List(totalCells) { AlignmentPlayer.NONE },
            size = boardDimension,
            maxPiecesPerPlayer = piecesToPlace
        )
    }

    override fun validateMove(state: AlignmentState, move: AlignmentMove): Boolean {
        if (state.winner != null || state.isDrawGame) return false
        return when (move) {
            is AlignmentMove.PlacePiece -> {
                if (state.phase != 1) return false
                if (move.index !in state.board.indices) return false
                state.board[move.index] == AlignmentPlayer.NONE
            }
            is AlignmentMove.MovePiece -> {
                if (state.phase != 2) return false
                if (move.fromIndex !in state.board.indices || move.toIndex !in state.board.indices) return false
                if (state.board[move.fromIndex] != state.currentTurn) return false
                if (state.board[move.toIndex] != AlignmentPlayer.NONE) return false
                isAdjacent(move.fromIndex, move.toIndex, state.size)
            }
            is AlignmentMove.SelectPiece -> true
        }
    }

    private fun isAdjacent(from: Int, to: Int, size: Int): Boolean {
        val r1 = from / size
        val c1 = from % size
        val r2 = to / size
        val c2 = to % size
        val rowDiff = kotlin.math.abs(r1 - r2)
        val colDiff = kotlin.math.abs(c1 - c2)
        // For 3x3 diagonal connections (like Morris, Shisima or Alquerque grid)
        return (rowDiff <= 1 && colDiff <= 1) && (rowDiff + colDiff > 0)
    }

    override fun getLegalMoves(state: AlignmentState): List<AlignmentMove> {
        if (state.winner != null || state.isDrawGame) return emptyList()
        val moves = mutableListOf<AlignmentMove>()
        if (state.phase == 1) {
            state.board.indices.forEach { idx ->
                if (state.board[idx] == AlignmentPlayer.NONE) {
                    moves.add(AlignmentMove.PlacePiece(idx))
                }
            }
        } else {
            state.board.indices.filter { state.board[it] == state.currentTurn }.forEach { from ->
                state.board.indices.filter { state.board[it] == AlignmentPlayer.NONE }.forEach { to ->
                    if (isAdjacent(from, to, state.size)) {
                        moves.add(AlignmentMove.MovePiece(from, to))
                    }
                }
            }
        }
        return moves
    }

    override fun applyMove(state: AlignmentState, move: AlignmentMove): AlignmentState {
        when (move) {
            is AlignmentMove.SelectPiece -> {
                return state.copy(selectedIndex = move.index)
            }
            is AlignmentMove.PlacePiece -> {
                if (!validateMove(state, move)) return state
                val newBoard = state.board.toMutableList()
                newBoard[move.index] = state.currentTurn

                val p1Placed = if (state.currentTurn == AlignmentPlayer.PLAYER_ONE) state.piecesPlacedP1 + 1 else state.piecesPlacedP1
                val p2Placed = if (state.currentTurn == AlignmentPlayer.PLAYER_TWO) state.piecesPlacedP2 + 1 else state.piecesPlacedP2

                val nextPhase = if (p1Placed >= state.maxPiecesPerPlayer && p2Placed >= state.maxPiecesPerPlayer) 2 else 1
                val winner = checkWinner(newBoard, state.size)
                val nextTurn = if (state.currentTurn == AlignmentPlayer.PLAYER_ONE) AlignmentPlayer.PLAYER_TWO else AlignmentPlayer.PLAYER_ONE

                return state.copy(
                    board = newBoard,
                    currentTurn = if (winner != null) state.currentTurn else nextTurn,
                    phase = nextPhase,
                    piecesPlacedP1 = p1Placed,
                    piecesPlacedP2 = p2Placed,
                    winner = winner,
                    selectedIndex = null,
                    moveCount = state.moveCount + 1
                )
            }
            is AlignmentMove.MovePiece -> {
                if (!validateMove(state, move)) return state
                val newBoard = state.board.toMutableList()
                newBoard[move.fromIndex] = AlignmentPlayer.NONE
                newBoard[move.toIndex] = state.currentTurn

                val winner = checkWinner(newBoard, state.size)
                val nextTurn = if (state.currentTurn == AlignmentPlayer.PLAYER_ONE) AlignmentPlayer.PLAYER_TWO else AlignmentPlayer.PLAYER_ONE

                return state.copy(
                    board = newBoard,
                    currentTurn = if (winner != null) state.currentTurn else nextTurn,
                    winner = winner,
                    selectedIndex = null,
                    moveCount = state.moveCount + 1
                )
            }
        }
    }

    private fun checkWinner(board: List<AlignmentPlayer>, size: Int): AlignmentPlayer? {
        if (size != 3) return null // Custom checkers for non-3x3
        // Rows
        for (r in 0 until 3) {
            val p = board[r * 3]
            if (p != AlignmentPlayer.NONE && p == board[r * 3 + 1] && p == board[r * 3 + 2]) return p
        }
        // Cols
        for (c in 0 until 3) {
            val p = board[c]
            if (p != AlignmentPlayer.NONE && p == board[c + 3] && p == board[c + 6]) return p
        }
        // Diagonals
        val center = board[4]
        if (center != AlignmentPlayer.NONE) {
            if (center == board[0] && center == board[8]) return center
            if (center == board[2] && center == board[6]) return center
        }
        return null
    }

    override fun getNextTurn(state: AlignmentState): Int =
        if (state.currentTurn == AlignmentPlayer.PLAYER_ONE) 1 else 2

    override fun isGameOver(state: AlignmentState): Boolean = state.winner != null || state.isDrawGame

    override fun getWinner(state: AlignmentState): Int? = when (state.winner) {
        AlignmentPlayer.PLAYER_ONE -> 1
        AlignmentPlayer.PLAYER_TWO -> 2
        else -> null
    }

    override fun isDraw(state: AlignmentState): Boolean = state.isDrawGame
}

// -------------------------------------------------------------
// 2. Mancala Engine (e.g., Kalah / Congklak / Oware / Hawalis)
// -------------------------------------------------------------
data class MancalaState(
    val pits: List<Int>, // 0-5 P1 pits, 6: P1 store, 7-12 P2 pits, 13: P2 store
    val currentTurnPlayer: Int = 1, // 1 or 2
    val isGameOver: Boolean = false,
    val winner: Int? = null,
    val messageAr: String = "دور اللاعب الأول"
)

data class MancalaMove(val pitIndex: Int)

class MancalaEngine(private val seedsPerPit: Int = 4) : GameEngine<MancalaState, MancalaMove> {

    override fun getInitialState(): MancalaState {
        // Standard 2x6 board + 2 stores
        val initialPits = MutableList(14) { 0 }
        for (i in 0..5) initialPits[i] = seedsPerPit
        for (i in 7..12) initialPits[i] = seedsPerPit
        return MancalaState(pits = initialPits)
    }

    override fun validateMove(state: MancalaState, move: MancalaMove): Boolean {
        if (state.isGameOver) return false
        val idx = move.pitIndex
        if (state.currentTurnPlayer == 1) {
            return idx in 0..5 && state.pits[idx] > 0
        } else {
            return idx in 7..12 && state.pits[idx] > 0
        }
    }

    override fun getLegalMoves(state: MancalaState): List<MancalaMove> {
        if (state.isGameOver) return emptyList()
        val range = if (state.currentTurnPlayer == 1) 0..5 else 7..12
        return range.filter { state.pits[it] > 0 }.map { MancalaMove(it) }
    }

    override fun applyMove(state: MancalaState, move: MancalaMove): MancalaState {
        if (!validateMove(state, move)) return state

        val pits = state.pits.toMutableList()
        var seeds = pits[move.pitIndex]
        pits[move.pitIndex] = 0

        var currentIdx = move.pitIndex
        val player = state.currentTurnPlayer
        val ownStore = if (player == 1) 6 else 13
        val opponentStore = if (player == 1) 13 else 6

        while (seeds > 0) {
            currentIdx = (currentIdx + 1) % 14
            // Skip opponent's store
            if (currentIdx == opponentStore) continue
            pits[currentIdx] += 1
            seeds -= 1
        }

        // Check if landed in empty own pit (Capture rule)
        val ownPitsRange = if (player == 1) 0..5 else 7..12
        var captured = false
        if (currentIdx in ownPitsRange && pits[currentIdx] == 1) {
            val oppositeIdx = 12 - currentIdx
            if (pits[oppositeIdx] > 0) {
                val totalStolen = pits[oppositeIdx] + 1
                pits[currentIdx] = 0
                pits[oppositeIdx] = 0
                pits[ownStore] += totalStolen
                captured = true
            }
        }

        // Did turn land in own store? If so, get free turn!
        val freeTurn = (currentIdx == ownStore)
        val nextPlayer = if (freeTurn) player else if (player == 1) 2 else 1

        // Check game over (either side empty)
        val p1Empty = (0..5).all { pits[it] == 0 }
        val p2Empty = (7..12).all { pits[it] == 0 }

        if (p1Empty || p2Empty) {
            // Sweep remaining seeds to respective stores
            for (i in 0..5) {
                pits[6] += pits[i]
                pits[i] = 0
            }
            for (i in 7..12) {
                pits[13] += pits[i]
                pits[i] = 0
            }
            val winner = when {
                pits[6] > pits[13] -> 1
                pits[13] > pits[6] -> 2
                else -> 0 // Draw
            }
            val msg = when (winner) {
                1 -> "فاز اللاعب الأول (${pits[6]} مقابل ${pits[13]})"
                2 -> "فاز اللاعب الثاني (${pits[13]} مقابل ${pits[6]})"
                else -> "تعادل حبات المانكالا (${pits[6]} مقابل ${pits[13]})"
            }
            return MancalaState(pits = pits, currentTurnPlayer = nextPlayer, isGameOver = true, winner = if (winner == 0) null else winner, messageAr = msg)
        }

        val msg = if (freeTurn) "دور إضافي للاعب $player!" else "دور اللاعب $nextPlayer"
        return MancalaState(pits = pits, currentTurnPlayer = nextPlayer, isGameOver = false, messageAr = msg)
    }

    override fun getNextTurn(state: MancalaState): Int = state.currentTurnPlayer
    override fun isGameOver(state: MancalaState): Boolean = state.isGameOver
    override fun getWinner(state: MancalaState): Int? = state.winner
    override fun isDraw(state: MancalaState): Boolean = state.isGameOver && state.winner == null
}

// -------------------------------------------------------------
// 3. Ancient Dice & Track Engine (e.g., Royal Game of Ur / Senet)
// -------------------------------------------------------------
data class DiceTrackState(
    val trackLength: Int = 14,
    val p1Positions: List<Int>, // 0 = off board start, 1..trackLength = on track, > trackLength = bear off
    val p2Positions: List<Int>,
    val piecesPerPlayer: Int = 4,
    val currentDiceRoll: Int? = null,
    val currentTurnPlayer: Int = 1,
    val winner: Int? = null,
    val rosetteIndices: Set<Int> = setOf(4, 8, 14),
    val messageAr: String = "ارمِ النرد لتبدأ"
)

sealed class DiceTrackMove {
    object RollDice : DiceTrackMove()
    data class AdvancePiece(val pieceIndex: Int) : DiceTrackMove()
}

class DiceTrackEngine(
    private val totalTrack: Int = 14,
    private val piecesCount: Int = 4
) : GameEngine<DiceTrackState, DiceTrackMove> {

    override fun getInitialState(): DiceTrackState {
        return DiceTrackState(
            trackLength = totalTrack,
            p1Positions = List(piecesCount) { 0 },
            p2Positions = List(piecesCount) { 0 },
            piecesPerPlayer = piecesCount
        )
    }

    override fun validateMove(state: DiceTrackState, move: DiceTrackMove): Boolean {
        if (state.winner != null) return false
        return when (move) {
            is DiceTrackMove.RollDice -> state.currentDiceRoll == null
            is DiceTrackMove.AdvancePiece -> {
                val roll = state.currentDiceRoll ?: return false
                if (roll == 0) return false // zero roll passes turn
                val pieces = if (state.currentTurnPlayer == 1) state.p1Positions else state.p2Positions
                if (move.pieceIndex !in pieces.indices) return false
                val currentPos = pieces[move.pieceIndex]
                val targetPos = currentPos + roll
                // Can't advance already borne off
                if (currentPos > state.trackLength) return false
                // Check if target is unoccupied by own piece
                !pieces.contains(targetPos)
            }
        }
    }

    override fun getLegalMoves(state: DiceTrackState): List<DiceTrackMove> {
        if (state.winner != null) return emptyList()
        val roll = state.currentDiceRoll
        if (roll == null) return listOf(DiceTrackMove.RollDice)
        if (roll == 0) return emptyList()

        val pieces = if (state.currentTurnPlayer == 1) state.p1Positions else state.p2Positions
        return pieces.indices.filter { idx ->
            validateMove(state, DiceTrackMove.AdvancePiece(idx))
        }.map { DiceTrackMove.AdvancePiece(it) }
    }

    override fun applyMove(state: DiceTrackState, move: DiceTrackMove): DiceTrackState {
        when (move) {
            is DiceTrackMove.RollDice -> {
                // Royal Game of Ur 4 tetrahedral dice: each has 50% chance of 1. Total: 0..4
                val roll = (1..4).count { Math.random() < 0.5 }
                if (roll == 0) {
                    // Turn skipped
                    val nextPlayer = if (state.currentTurnPlayer == 1) 2 else 1
                    return state.copy(
                        currentDiceRoll = null,
                        currentTurnPlayer = nextPlayer,
                        messageAr = "النتيجة 0! انتقل الدور للاعب $nextPlayer"
                    )
                }
                return state.copy(
                    currentDiceRoll = roll,
                    messageAr = "النتيجة: $roll. اختر قطعة للتحريك"
                )
            }
            is DiceTrackMove.AdvancePiece -> {
                val roll = state.currentDiceRoll ?: return state
                val p1 = state.p1Positions.toMutableList()
                val p2 = state.p2Positions.toMutableList()
                val isP1 = state.currentTurnPlayer == 1

                val myPieces = if (isP1) p1 else p2
                val oppPieces = if (isP1) p2 else p1

                val currentPos = myPieces[move.pieceIndex]
                val targetPos = currentPos + roll
                myPieces[move.pieceIndex] = targetPos

                // Capture opponent if in shared middle track (indices 5..12) and not safe rosette (8)
                if (targetPos in 5..12 && targetPos != 8) {
                    val capturedIdx = oppPieces.indexOf(targetPos)
                    if (capturedIdx != -1) {
                        oppPieces[capturedIdx] = 0 // Reset to start
                    }
                }

                // Check winner: all pieces > trackLength
                val win = myPieces.all { it > state.trackLength }
                if (win) {
                    return state.copy(
                        p1Positions = p1,
                        p2Positions = p2,
                        winner = state.currentTurnPlayer,
                        currentDiceRoll = null,
                        messageAr = "فاز اللاعب ${state.currentTurnPlayer} بإنهاء جميع قطعه!"
                    )
                }

                // Rosette grants another turn
                val landsOnRosette = state.rosetteIndices.contains(targetPos)
                val nextPlayer = if (landsOnRosette) state.currentTurnPlayer else if (isP1) 2 else 1
                val msg = if (landsOnRosette) "زهرة الروزيت! دور إضافي للاعب ${state.currentTurnPlayer}." else "دور اللاعب $nextPlayer"

                return state.copy(
                    p1Positions = p1,
                    p2Positions = p2,
                    currentDiceRoll = null,
                    currentTurnPlayer = nextPlayer,
                    messageAr = msg
                )
            }
        }
    }

    override fun getNextTurn(state: DiceTrackState): Int = state.currentTurnPlayer
    override fun isGameOver(state: DiceTrackState): Boolean = state.winner != null
    override fun getWinner(state: DiceTrackState): Int? = state.winner
    override fun isDraw(state: DiceTrackState): Boolean = false
}
