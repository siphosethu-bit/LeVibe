package com.example.levibegg.data

data class Event(
    val id: String,
    val name: String,
    val city: String,
    val venue: String,
    val date: String,
    val time: String,
    val priceFrom: String,
    val latitude: Double,
    val longitude: Double
)

// Temporary fake data for LeVibe MVP
val sampleEvents = listOf(
    Event(
        id = "1",
        name = "RAMFEST x HALLOWEEN 2025 | Cape Town",
        city = "Cape Town",
        venue = "Terminal X • Paarden Eiland",
        date = "31 Oct 2025",
        time = "16:00 – 01:00",
        priceFrom = "From R120",
        latitude = -33.918861,
        longitude = 18.484711
    ),
    Event(
        id = "2",
        name = "Sundowners with Alex O'Rion",
        city = "Cape Town",
        venue = "Rooftop on Bree",
        date = "31 Oct 2025",
        time = "17:00 – 23:00",
        priceFrom = "From R150",
        latitude = -33.918,
        longitude = 18.42
    )
)
