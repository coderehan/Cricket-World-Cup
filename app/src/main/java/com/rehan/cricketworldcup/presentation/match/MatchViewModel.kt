package com.rehan.cricketworldcup.presentation.match

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rehan.cricketworldcup.data.model.BallOutcome
import com.rehan.cricketworldcup.data.model.Innings
import com.rehan.cricketworldcup.data.model.Team
import com.rehan.cricketworldcup.data.repository.TeamRepository
import com.rehan.cricketworldcup.domain.engine.MatchEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel responsible for managing the complete cricket match.
 *
 * Responsibilities:
 * - Initialize both teams and innings.
 * - Maintain Match UI state.
 * - Delegate every delivery to MatchEngine.
 * - Switch innings.
 * - Finish match.
 */
@HiltViewModel
class MatchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: TeamRepository,
    private val matchEngine: MatchEngine
) : ViewModel() {

    /**
     * Team names received from Navigation.
     */
    private val team1Name =
        savedStateHandle.get<String>("team1").orEmpty()

    private val team2Name =
        savedStateHandle.get<String>("team2").orEmpty()

    /**
     * Fetch all available teams.
     *
     * This is a cheap in-memory lookup once TeamRepositoryImpl's cache
     * has been populated by TeamSelectionViewModel's earlier IO read.
     */
    private val allTeams = repository.getTeams()

    /**
     * Retrieve complete Team objects including flags.
     *
     * Using firstOrNull() with an explicit error instead of first {},
     * so a nav-arg mismatch fails with a clear message instead of an
     * unhandled NoSuchElementException.
     */
    private val team1 =
        allTeams.firstOrNull { it.name == team1Name }
            ?: error("Team not found for name: $team1Name")

    private val team2 =
        allTeams.firstOrNull { it.name == team2Name }
            ?: error("Team not found for name: $team2Name")

    /**
     * Backing StateFlow.
     */
    private val _uiState = MutableStateFlow(

        MatchUiState(

            firstInnings = Innings(
                battingTeam = team1
            ),

            secondInnings = Innings(
                battingTeam = team2
            ),

            currentBattingTeam = team1
        )
    )

    /**
     * Public immutable UI state.
     */
    val uiState: StateFlow<MatchUiState> =
        _uiState.asStateFlow()

    /**
     * Called whenever user presses
     * "Play Next Ball".
     */
    fun playNextBall() {

        if (_uiState.value.isMatchOver) return

        if (_uiState.value.isFirstInnings) {
            playFirstInnings()
        } else {
            playSecondInnings()
        }
    }

    /**
     * Simulates one delivery of the first innings.
     */
    private fun playFirstInnings() {

        val (updatedInnings, outcome) =
            matchEngine.playNextBall(
                _uiState.value.firstInnings
            )

        _uiState.value = _uiState.value.copy(

            firstInnings = updatedInnings,

            lastBall = ballOutcomeText(outcome)
        )

        if (matchEngine.isInningsOver(updatedInnings)) {

            finishFirstInnings()
        }
    }

    /**
     * Starts second innings.
     */
    private fun finishFirstInnings() {

        _uiState.value = _uiState.value.copy(

            isFirstInnings = false,

            currentBattingTeam =
                _uiState.value.secondInnings.battingTeam,

            lastBall = "Innings Break"
        )
    }

    /**
     * Simulates one delivery of second innings.
     */
    private fun playSecondInnings() {

        val (updatedInnings, outcome) =
            matchEngine.playNextBall(
                _uiState.value.secondInnings
            )

        _uiState.value = _uiState.value.copy(

            secondInnings = updatedInnings,

            lastBall = ballOutcomeText(outcome)
        )

        val firstInningsScore =
            _uiState.value.firstInnings.runs

        /**
         * Chasing team wins immediately.
         */
        if (

            matchEngine.hasChasedTarget(

                currentRuns = updatedInnings.runs,

                target = firstInningsScore

            )
        ) {

            finishMatch(updatedInnings.battingTeam)

            return
        }

        /**
         * Second innings completed.
         */
        if (

            matchEngine.isInningsOver(updatedInnings)

        ) {

            val winner = when {

                updatedInnings.runs > firstInningsScore ->
                    updatedInnings.battingTeam

                updatedInnings.runs < firstInningsScore ->
                    _uiState.value.firstInnings.battingTeam

                else ->
                    null
            }

            finishMatch(winner)
        }
    }

    /**
     * Ends the match.
     */
    private fun finishMatch(
        winner: Team?
    ) {

        _uiState.value = _uiState.value.copy(

            isMatchOver = true,

            winner = winner
        )
    }

    /**
     * Converts BallOutcome into display text.
     */
    private fun ballOutcomeText(
        outcome: BallOutcome
    ): String {

        return when (outcome) {

            is BallOutcome.Runs ->
                outcome.runs.toString()

            BallOutcome.Wicket ->
                "OUT"

            BallOutcome.Wide ->
                "WD"

            BallOutcome.NoBall ->
                "NB"
        }
    }
}