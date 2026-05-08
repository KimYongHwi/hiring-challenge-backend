package com.spoqa.hiringchallenge.challenge

data class HealthResponse(
    val status: String
)

data class ChallengeSummaryResponse(
    val service: String,
    val version: String,
    val message: String
)
