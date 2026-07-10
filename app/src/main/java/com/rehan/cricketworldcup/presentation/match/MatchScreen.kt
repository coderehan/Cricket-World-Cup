package com.rehan.cricketworldcup.presentation.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rehan.cricketworldcup.presentation.match.components.BallResultView
import com.rehan.cricketworldcup.presentation.match.components.PlayBallButton
import com.rehan.cricketworldcup.presentation.match.components.TeamScoreCard

/**
 * Match Center Screen.
 *
 * Displays:
 * - Score cards for both innings (batting team on top, bowling team below).
 * - A center panel showing either the outcome of the last ball played,
 *   or the final match result once the match is over.
 * - A button to simulate the next delivery. Once the match ends, this
 *   same button relabels to "Match Over" and navigates back to the
 *   Team Selection screen when tapped.
 *
 * @param onMatchOver Invoked when the user taps the button after the
 * match has ended, so the caller can navigate back to Team Selection.
 */
@Composable
fun MatchScreen(
    viewModel: MatchViewModel,
    onMatchOver: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    /**
     * Innings belonging to the team currently batting is always shown first
     * ("Pakistan (Batting)" on top, "India (Bowling)" below).
     */
    val battingInnings =
        if (uiState.isFirstInnings) uiState.firstInnings else uiState.secondInnings

    val bowlingInnings =
        if (uiState.isFirstInnings) uiState.secondInnings else uiState.firstInnings

    /**
     * Text shown inside the result panel.
     * Once the match is over, this switches from the last ball's
     * outcome to the final result ("India Wins", "Pakistan Wins", or "Match Tied")
     */
    val resultPanelText = if (uiState.isMatchOver) {

        uiState.winner?.let { winner -> "${winner.name} Wins" }
            ?: "Match Tied"

    } else {

        uiState.lastBall
    }

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Match Center",
                style = MaterialTheme.typography.headlineSmall
            )

            /**
             * Batting team's card.
             */
            TeamScoreCard(
                innings = battingInnings,
                isBatting = true
            )

            /**
             * Bowling team's card.
             */
            TeamScoreCard(
                innings = bowlingInnings,
                isBatting = false
            )

            /**
             * Result panel. Shows the last ball's outcome while the
             * match is in progress, or the final result once it ends.
             */
            BallResultView(
                lastBall = resultPanelText,
                modifier = Modifier.weight(1f)
            )

            /**
             * Before match over: plays the next delivery.
             * After match over: label switches to "Match Over" and
             * tapping it navigates back to Team Selection.
             *
             * The button stays enabled in both states — it always
             * needs to be tappable, just its action changes.
             */
            PlayBallButton(
                enabled = true,
                isMatchOver = uiState.isMatchOver,
                onClick = {
                    if (uiState.isMatchOver) {
                        onMatchOver()
                    } else {
                        viewModel.playNextBall()
                    }
                }
            )
        }
    }
}