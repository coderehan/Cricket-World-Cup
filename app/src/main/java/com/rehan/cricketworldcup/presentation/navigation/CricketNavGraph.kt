package com.rehan.cricketworldcup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rehan.cricketworldcup.presentation.match.MatchScreen
import com.rehan.cricketworldcup.presentation.match.MatchViewModel
import com.rehan.cricketworldcup.presentation.teamselection.TeamSelectionScreen
import com.rehan.cricketworldcup.presentation.teamselection.TeamSelectionViewModel

/**
 * Root navigation graph of the application.
 *
 * Contains all navigation destinations.
 */
@Composable
fun CricketNavGraph(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.TeamSelection.route,
        modifier = modifier
    ) {

        /**
         * Team Selection Screen
         */
        composable(Screen.TeamSelection.route) {

            val viewModel: TeamSelectionViewModel = hiltViewModel()

            TeamSelectionScreen(
                viewModel = viewModel,
                onStartMatch = {

                    val selectedTeams = viewModel.getSelectedTeams()

                    navController.navigate(
                        Screen.Match.createRoute(
                            team1 = selectedTeams[0].name,
                            team2 = selectedTeams[1].name
                        )
                    )
                }
            )
        }

        /**
         * Match Screen
         */
        composable(
            route = Screen.Match.route
        ) {

            val viewModel: MatchViewModel = hiltViewModel()

            MatchScreen(
                viewModel = viewModel,
                onMatchOver = {
                    navController.popBackStack(
                        route = Screen.TeamSelection.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}