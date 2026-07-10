package com.rehan.cricketworldcup.presentation.match.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rehan.cricketworldcup.data.model.Innings

/**
 * Reusable score card for a team's innings.
 *
 * This composable displays:
 * - Team Flag
 * - Team Name
 * - Score
 * - Overs
 * - Batting/Waiting status
 */
@Composable
fun TeamScoreCard(
    innings: Innings,
    isBatting: Boolean,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = innings.battingTeam.flag,
                contentDescription = innings.battingTeam.name
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),

                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = innings.battingTeam.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${innings.runs}/${innings.wickets}",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = formatOvers(innings.balls),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = if (isBatting) "Batting" else "Bowling",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/**
 * Converts balls into cricket overs.
 *
 * Examples:
 *
 * 0 -> 0.0
 * 1 -> 0.1
 * 5 -> 0.5
 * 6 -> 1.0
 * 7 -> 1.1
 * 12 -> 2.0
 */
private fun formatOvers(
    balls: Int
): String {

    val overs = balls / 6
    val remainingBalls = balls % 6

    return "$overs.$remainingBalls Overs"
}