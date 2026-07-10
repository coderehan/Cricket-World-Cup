package com.rehan.cricketworldcup.presentation.match.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Reusable button used to play the next delivery.
 *
 * The button becomes disabled and its label switches to "Match Over"
 * once the match finishes, matching the reference screenshots.
 */
@Composable
fun PlayBallButton(
    enabled: Boolean,
    isMatchOver: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        Text(
            text = if (isMatchOver) "MATCH OVER" else "PLAY NEXT BALL",
            style = MaterialTheme.typography.titleMedium
        )
    }
}