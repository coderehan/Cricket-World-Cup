package com.rehan.cricketworldcup.data.repository

import com.rehan.cricketworldcup.data.datasource.TeamDataSource
import com.rehan.cricketworldcup.data.model.Team
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of TeamRepository.
 *
 * Currently, the data comes from a local JSON file using TeamDataSource.
 *
 * Teams are cached in memory after the first successful read. This is
 * important because TeamSelectionViewModel reads teams on Dispatchers.IO,
 * but MatchViewModel needs the full Team objects synchronously during
 * construction (to resolve team1/team2 from nav args). Caching means the
 * second call is a plain in-memory list lookup instead of a second disk read.
 */
@Singleton
class TeamRepositoryImpl @Inject constructor(
    private val teamDataSource: TeamDataSource
) : TeamRepository {

    private var cachedTeams: List<Team>? = null

    @Synchronized
    override fun getTeams(): List<Team> {

        cachedTeams?.let { return it }

        val teams = teamDataSource.getTeams()

        cachedTeams = teams

        return teams
    }
}