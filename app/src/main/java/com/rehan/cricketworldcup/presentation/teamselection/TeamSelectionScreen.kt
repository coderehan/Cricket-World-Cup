package com.rehan.cricketworldcup.presentation.teamselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rehan.cricketworldcup.presentation.teamselection.components.TeamItem

/**
 * Team Selection Screen.
 *
 * Displays all cricket teams and allows the user
 * to select exactly two teams before starting the match.
 */
@Composable
fun TeamSelectionScreen(
    viewModel: TeamSelectionViewModel,
    onStartMatch: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->

        when {

            /**
             * Loading state.
             */
            uiState.isLoading -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()
                }
            }

            /**
             * Error state.
             */
            uiState.error != null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = uiState.error ?: "Unknown Error",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            /**
             * Success state.
             */
            else -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    Text(
                        text = "Select Two Teams",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(
                            items = uiState.teams,
                            key = { it.name }
                        ) { team ->

                            TeamItem(
                                team = team,
                                isSelected = uiState.selectedTeams.contains(team),
                                onClick = {
                                    viewModel.onTeamSelected(team)
                                }
                            )
                        }
                    }

                    Button(
                        onClick = onStartMatch,
                        enabled = uiState.canStartMatch,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "START MATCH",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}