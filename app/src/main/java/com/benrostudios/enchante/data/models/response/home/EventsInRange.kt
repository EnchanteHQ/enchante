package com.benrostudios.enchante.data.models.response.home

data class EventsInRange(
    val availableSeats: Int? = 0,
    val event: Event = Event()
)