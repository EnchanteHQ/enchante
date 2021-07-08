package com.benrostudios.enchante.data.models.response.events

data class PastEvent(
    val _id: String? = "",
    val eventId: EventId = EventId(),
    val passType: String? = ""
)