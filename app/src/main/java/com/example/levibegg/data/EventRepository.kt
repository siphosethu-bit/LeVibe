package com.example.levibegg.data

import com.example.levibegg.model.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

class EventRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun eventsFlow(): Flow<List<Event>> = callbackFlow {
        val reg = db.collection("events").addSnapshotListener { snap, _ ->
            if (snap != null) {
                val list = snap.documents.mapNotNull { d ->
                    d.toObject(Event::class.java)?.copy(id = d.id)
                }
                trySend(list)
            }
        }
        awaitClose { reg.remove() }
    }
}
