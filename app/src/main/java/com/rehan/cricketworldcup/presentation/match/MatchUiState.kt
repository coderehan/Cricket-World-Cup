package com.rehan.cricketworldcup.presentation.match

import com.rehan.cricketworldcup.data.model.Innings
import com.rehan.cricketworldcup.data.model.Team

/**
 * Represents the complete UI state of the Match screen.
 *
 * The ViewModel updates this state after every ball,
 * and Compose automatically recomposes the UI.
 */
data class MatchUiState(

    /**
     * First innings.
     */
    val firstInnings: Innings,

    /**
     * Second innings.
     */
    val secondInnings: Innings,

    /**
     * Team currently batting.
     */
    val currentBattingTeam: Team,

    /**
     * Result of the previous delivery.
     */
    val lastBall: String = "-",

    /**
     * True while first innings is in progress.
     */
    val isFirstInnings: Boolean = true,

    /**
     * True once the match finishes.
     */
    val isMatchOver: Boolean = false,

    /**
     * Winner of the match.
     * Null indicates either the match is ongoing or it ended in a tie.
     */
    val winner: Team? = null
)