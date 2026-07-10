package com.rehan.cricketworldcup.domain.random

import com.rehan.cricketworldcup.data.model.BallOutcome
import kotlin.random.Random
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Responsible for generating the outcome of every ball.
 *
 * The probability distribution is intentionally weighted
 * so that dot balls and singles occur more frequently,
 * while sixes and wickets are comparatively rare.
 *
 * This class contains NO cricket rules.
 * It only decides "what happened on this delivery".
 */
@Singleton
class RandomOutcomeGenerator @Inject constructor() {

    /**
     * Generates a random outcome for the next delivery.
     */
    fun nextBall(): BallOutcome {

        val random = Random.nextInt(100)

        return when (random) {

            in 0..24 -> BallOutcome.Runs(0)      // 25%

            in 25..49 -> BallOutcome.Runs(1)     // 25%

            in 50..62 -> BallOutcome.Runs(2)     // 13%

            in 63..69 -> BallOutcome.Runs(3)     // 7%

            in 70..82 -> BallOutcome.Runs(4)     // 13%

            in 83..89 -> BallOutcome.Runs(6)     // 7%

            in 90..95 -> BallOutcome.Wicket      // 6%

            96,97 -> BallOutcome.Wide            // 2%

            else -> BallOutcome.NoBall           // 2%
        }
    }
}