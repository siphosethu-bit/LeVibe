package com.example.levibegg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levibegg.data.Event
import com.example.levibegg.data.sampleEvents
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun EventsMapScreen() {
    val capeTown = LatLng(-33.918861, 18.423300)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(capeTown, 6.5f)
    }

    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050814))
    ) {
        Text(
            text = "Explore events",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            ) {
                sampleEvents.forEach { event ->
                    val position = LatLng(event.latitude, event.longitude)
                    Marker(
                        state = MarkerState(position),
                        title = event.name,
                        snippet = event.venue,
                        onClick = {
                            selectedEvent = event
                            false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        EventCardsRow(
            events = sampleEvents,
            selectedEvent = selectedEvent,
            onEventSelected = { event ->
                selectedEvent = event
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(event.latitude, event.longitude),
                    11f
                )
            }
        )
    }
}

@Composable
private fun EventCardsRow(
    events: List<Event>,
    selectedEvent: Event?,
    onEventSelected: (Event) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(events) { event ->
            val isSelected = event.id == selectedEvent?.id
            Card(
                modifier = Modifier
                    .width(260.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        Color(0xFF20263A) else Color(0xFF151826)
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                onClick = { onEventSelected(event) }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(event.name, color = Color.White, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("${event.venue} • ${event.city}", color = Color(0xFFB0B3C0), fontSize = 11.sp)
                    Text("${event.date} • ${event.time}", color = Color(0xFFB0B3C0), fontSize = 11.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(event.priceFrom, color = Color(0xFFFFC857), fontSize = 12.sp)
                }
            }
        }
    }
}

