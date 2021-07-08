package com.benrostudios.enchante.data.models.response.events

data class FutureEvent(
    val _id: String? = "",
    val budget: String? = "",
    val eventId: EventId? = EventId(),
    val passType: String? = ""
)