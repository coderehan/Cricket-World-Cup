package com.rehan.cricketworldcup.presentation.teamselection

import com.rehan.cricketworldcup.data.model.Team

/**
 * Represents the complete UI state of the Team Selection screen.
 *
 * Compose observes this object.
 * Whenever any value changes, the screen automatically recomposes.
 *
 * Keeping everything inside a single UiState makes the ViewModel
 * easier to maintain and follows the Single Source of Truth principle.
 */
data class TeamSelectionUiState(

    /**
     * List of all available teams loaded from teams.json.
     */
    val teams: List<Team> = emptyList(),

    /**
     * Stores the two teams selected by the user.
     *
     * We use a List because:
     * - Selection order matters.
     * - Maximum allowed size is 2.
     */
    val selectedTeams: List<Team> = emptyList(),

    /**
     * Displays loading while reading JSON.
     *
     * Although reading assets is very fast, this is good
     * architecture and keeps the UI extensible.
     */
    val isLoading: Boolean = false,

    /**
     * Used to display any unexpected error.
     *
     * Example:
     * - Missing JSON
     * - Invalid JSON
     */
    val error: String? = null
) {

    /**
     * Convenience property.
     *
     * Instead of writing:
     *
     * uiState.selectedTeams.size == 2
     *
     * everywhere,
     * the UI simply checks:
     *
     * uiState.canStartMatch
     */
    val canStartMatch: Boolean
        get() = selectedTeams.size == 2
}