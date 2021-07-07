package com.benrostudios.enchante.data.network.reponse

import com.benrostudios.enchante.data.models.response.home.EventsBasedOnYourInterest
import com.benrostudios.enchante.data.models.response.home.EventsInRange
import com.benrostudios.enchante.data.models.response.home.TipDetails

data class ResponseMetrics(
    val jwtToken: String?,
    val eventsBasedOnYourInterest: List<EventsBasedOnYourInterest>? = listOf(),
    val eventsInRange: List<EventsInRange>? = listOf(),
    val tip: TipDetails? = TipDetails()
)