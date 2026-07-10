package com.rehan.cricketworldcup.data.model

data class Innings(

    val battingTeam: Team,

    val runs: Int = 0,

    val wickets: Int = 0,

    val balls: Int = 0

)