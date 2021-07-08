package com.benrostudios.enchante.data.models.response.events

data class MyEventsResponse(
    val futureEvents: List<FutureEvent>? = listOf(),
    val pastEvents: List<PastEvent>? = listOf()
)