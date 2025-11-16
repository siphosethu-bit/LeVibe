package com.example.levibegg.data

data class Ticket(
    val id: String,
    val eventId: String,
    val eventTitle: String,
    val eventDateLabel: String,
    val location: String,
    val buyerName: String,
    val buyerEmail: String,
    val quantity: Int,
    val priceLabel: String,   // e.g. "R350 / ticket"
    val purchasedAt: Long
)
