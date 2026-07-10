package com.rehan.cricketworldcup.presentation.teamselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.cricketworldcup.data.model.Team
import com.rehan.cricketworldcup.data.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Team Selection screen.
 *
 * Responsibilities:
 * -----------------
 * 1. Load teams from the repository.
 * 2. Maintain the UI state.
 * 3. Handle team selection and deselection.
 * 4. Expose immutable StateFlow to the UI.
 *
 * NOTE:
 * This ViewModel never talks directly to the JSON file.
 * It only communicates with the Repository.
 */
@HiltViewModel
class TeamSelectionViewModel @Inject constructor(
    private val repository: TeamRepository
) : ViewModel() {

    /**
     * Internal mutable state.
     *
     * Only this ViewModel can modify it.
     */
    private val _uiState = MutableStateFlow(TeamSelectionUiState())

    /**
     * Immutable state exposed to Compose.
     *
     * The UI can observe this but cannot modify it.
     */
    val uiState: StateFlow<TeamSelectionUiState> = _uiState.asStateFlow()

    /**
     * Automatically load the teams when
     * the ViewModel is created.
     */
    init {
        loadTeams()
    }

    /**
     * Reads all teams from the repository.
     *
     * Since file reading is an I/O operation,
     * it is executed on Dispatchers.IO.
     */
    private fun loadTeams() {

        viewModelScope.launch(Dispatchers.IO) {

            _uiState.update {
                it.copy(isLoading = true)
            }

            try {

                val teams = repository.getTeams()

                _uiState.update {
                    it.copy(
                        teams = teams,
                        isLoading = false
                    )
                }

            } catch (exception: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    /**
     * Handles team selection.
     *
     * Rules:
     * ------
     * 1. User can select maximum two teams.
     * 2. Clicking an already selected team deselects it.
     */
    fun onTeamSelected(team: Team) {

        val selectedTeams = _uiState.value.selectedTeams.toMutableList()

        when {

            /**
             * Team already selected.
             * Remove it.
             */
            selectedTeams.contains(team) -> {
                selectedTeams.remove(team)
            }

            /**
             * Less than two teams selected.
             * Add new team.
             */
            selectedTeams.size < 2 -> {
                selectedTeams.add(team)
            }

            /**
             * Already selected two teams.
             * Ignore further selections.
             */
            else -> return
        }

        _uiState.update {
            it.copy(selectedTeams = selectedTeams)
        }
    }

    /**
     * Returns the two selected teams.
     *
     * This will be used while navigating
     * to the Match Screen.
     */
    fun getSelectedTeams(): List<Team> {
        return _uiState.value.selectedTeams
    }
}