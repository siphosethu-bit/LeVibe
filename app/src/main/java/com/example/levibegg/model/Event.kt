package com.example.levibegg.model

data class Event(
    val id: String = "",
    val name: String = "",
    val city: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val startsAt: Long = 0L
)
