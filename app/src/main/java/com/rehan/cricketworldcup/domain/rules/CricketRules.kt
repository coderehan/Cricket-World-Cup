package com.rehan.cricketworldcup.domain.rules

import com.rehan.cricketworldcup.data.model.Innings
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Contains all cricket-related rules
 * Keeping these rules separate from MatchEngine
 */
@Singleton
class CricketRules @Inject constructor() {

    companion object {

        /**
         * Maximum overs
         */
        const val MAX_BALLS = 12

        /**
         * Innings ends after losing three wickets.
         */
        const val MAX_WICKETS = 3
    }

    /**
     * Returns true if the innings is complete.
     */
    fun isInningsOver(innings: Innings): Boolean {

        return innings.balls >= MAX_BALLS ||
                innings.wickets >= MAX_WICKETS
    }

    /**
     * Returns true if chasing team has already won.
     */
    fun hasChasedTarget(
        currentRuns: Int,
        target: Int
    ): Boolean {

        return currentRuns > target
    }
}