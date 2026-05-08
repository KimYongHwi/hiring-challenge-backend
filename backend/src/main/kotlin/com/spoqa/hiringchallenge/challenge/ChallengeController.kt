package com.spoqa.hiringchallenge.challenge

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ChallengeController {

    @GetMapping("/health")
    fun health(): HealthResponse = HealthResponse(status = "ok")

    @GetMapping("/challenge-summary")
    fun challengeSummary(): ChallengeSummaryResponse =
        ChallengeSummaryResponse(
            service = "spoqa-hiring-fullstack-challenge-api",
            version = "0.0.1",
            message = "Backend scaffold is ready for candidate assignment."
        )
}
