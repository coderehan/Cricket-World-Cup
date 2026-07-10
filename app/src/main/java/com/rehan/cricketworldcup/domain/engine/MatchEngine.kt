package com.rehan.cricketworldcup.domain.engine

import com.rehan.cricketworldcup.data.model.BallOutcome
import com.rehan.cricketworldcup.data.model.Innings
import com.rehan.cricketworldcup.domain.random.RandomOutcomeGenerator
import com.rehan.cricketworldcup.domain.rules.CricketRules
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Core cricket engine.
 *
 * Responsible for updating an innings after every delivery.
 *
 * It delegates:
 * - Random ball generation -> RandomOutcomeGenerator
 * - Cricket rules -> CricketRules
 */
@Singleton
class MatchEngine @Inject constructor(
    private val randomOutcomeGenerator: RandomOutcomeGenerator,
    private val cricketRules: CricketRules
) {

    /**
     * Simulates a single delivery.
     *
     * Returns:
     * 1. Updated innings
     * 2. Outcome of the delivery
     */
    fun playNextBall(
        innings: Innings
    ): Pair<Innings, BallOutcome> {

        val outcome = randomOutcomeGenerator.nextBall()

        val updatedInnings = when (outcome) {

            is BallOutcome.Runs -> {
                innings.copy(
                    runs = innings.runs + outcome.runs,
                    balls = innings.balls + 1
                )
            }

            BallOutcome.Wicket -> {
                innings.copy(
                    wickets = innings.wickets + 1,
                    balls = innings.balls + 1
                )
            }

            BallOutcome.Wide -> {
                innings.copy(
                    runs = innings.runs + 1
                )
            }

            BallOutcome.NoBall -> {
                innings.copy(
                    runs = innings.runs + 1
                )
            }
        }

        return updatedInnings to outcome
    }

    /**
     * Returns true if the innings has ended.
     */
    fun isInningsOver(
        innings: Innings
    ): Boolean {
        return cricketRules.isInningsOver(innings)
    }

    /**
     * Returns true if the chasing team has already won.
     */
    fun hasChasedTarget(
        currentRuns: Int,
        target: Int
    ): Boolean {
        return cricketRules.hasChasedTarget(
            currentRuns = currentRuns,
            target = target
        )
    }
}