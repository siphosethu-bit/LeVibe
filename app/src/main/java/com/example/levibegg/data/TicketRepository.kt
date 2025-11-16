package com.example.levibegg.data

import androidx.compose.runtime.mutableStateListOf

object TicketRepository {

    // observable list for Compose
    private val _tickets = mutableStateListOf<Ticket>()
    val tickets: List<Ticket> get() = _tickets

    fun addTicket(ticket: Ticket) {
        _tickets.add(ticket)
    }
}
