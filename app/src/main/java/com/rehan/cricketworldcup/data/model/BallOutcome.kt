package com.rehan.cricketworldcup.data.model

sealed class BallOutcome {

    data class Runs(
        val runs: Int
    ) : BallOutcome()

    data object Wicket : BallOutcome()

    data object Wide : BallOutcome()

    data object NoBall : BallOutcome()
}