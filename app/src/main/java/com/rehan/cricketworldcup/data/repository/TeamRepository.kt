package com.rehan.cricketworldcup.data.repository

import com.rehan.cricketworldcup.data.model.Team

/**
 * Repository acts as a single source of truth for the ViewModel.
 *
 * The ViewModel doesn't know whether the data is coming from:
 * - Assets
 * - Network
 * - Room Database
 * - Cache
 *
 * It simply requests the data from the repository.
 */
interface TeamRepository {

    /**
     * Returns the list of available cricket teams.
     */
    fun getTeams(): List<Team>
}