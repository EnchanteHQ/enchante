package com.benrostudios.enchante.data.models.response.home

data class EventsBasedOnYourInterest(
    val availableSeats: Int? = 0,
    val event: Event? = Event()
)