package com.rehan.cricketworldcup.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rehan.cricketworldcup.data.model.Team
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * DataSource is responsible for reading the local JSON file from the assets folder.
 *
 * It does NOT contain any business logic.
 * It simply reads the JSON and converts it into a list of Team objects.
 *
 * Since the assignment explicitly mentions that no network call is required,
 * this acts as our local source of truth.
 */
class TeamDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Reads teams.json from the assets folder
     * and converts it into List<Team> using Gson.
     */
    fun getTeams(): List<Team> {

        val inputStream = context.assets.open("teams.json")

        val reader = InputStreamReader(inputStream)

        val teamType = object : TypeToken<List<Team>>() {}.type

        return Gson().fromJson(reader, teamType)
    }
}