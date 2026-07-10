package com.rehan.cricketworldcup.presentation.navigation

sealed class Screen(val route: String) {

    data object TeamSelection : Screen("team_selection")

    data object Match : Screen(
        "match/{team1}/{team2}"
    ) {

        fun createRoute(
            team1: String,
            team2: String
        ): String {
            return "match/$team1/$team2"
        }
    }
}