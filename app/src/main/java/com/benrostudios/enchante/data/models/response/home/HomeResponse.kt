package com.benrostudios.enchante.data.models.response.home

data class HomeResponse(
    val eventsBasedOnYourInterest: List<EventsBasedOnYourInterest>? = listOf(),
    val eventsInRange: List<EventsInRange>? = listOf(),
    val tip: TipDetails? = TipDetails()
)