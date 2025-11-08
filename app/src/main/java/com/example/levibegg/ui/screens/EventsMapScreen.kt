package com.example.levibegg.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Simple event model for the map UI.
data class MapEvent(
    val id: String,
    val title: String,
    val city: String,
    val venue: String,
    val priceFrom: String,
    val dateLabel: String,
    val position: LatLng
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsMapScreen(
    onArtistsClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    // Demo events (swap with real data later)
    val events = remember {
        listOf(
            MapEvent(
                id = "jhb1",
                title = "Downtown Nights",
                city = "Johannesburg",
                venue = "Newtown Junction",
                priceFrom = "From R120",
                dateLabel = "Sat, Nov 15",
                position = LatLng(-26.2041, 28.0473)
            ),
            MapEvent(
                id = "cpt1",
                title = "Terminal X • Paarden Eiland",
                city = "Cape Town",
                venue = "Terminal X",
                priceFrom = "From R150",
                dateLabel = "Sat, Dec 6",
                position = LatLng(-33.9180, 18.4219)
            ),
            MapEvent(
                id = "dbn1",
                title = "Beachside Groove",
                city = "Durban",
                venue = "North Beach",
                priceFrom = "From R80",
                dateLabel = "Fri, Nov 28",
                position = LatLng(-29.8587, 31.0218)
            ),
            MapEvent(
                id = "bloem1",
                title = "Amapiano Nights",
                city = "Bloemfontein",
                venue = "CBD Warehouse",
                priceFrom = "From R100",
                dateLabel = "Sat, Dec 13",
                position = LatLng(-29.0852, 26.1596)
            )
        )
    }

    // City filter options
    val cityOptions = remember(events) {
        listOf("All South Africa") + events.map { it.city }.distinct()
    }

    var selectedCity by remember { mutableStateOf(cityOptions.first()) }
    var selectedEvent by remember { mutableStateOf<MapEvent?>(null) }

    // Camera state
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-29.0, 24.0), // center over SA
            5.2f
        )
    }

    // Events that match the selected city
    val filteredEvents = remember(selectedCity, events) {
        if (selectedCity == "All South Africa") events
        else events.filter { it.city == selectedCity }
    }

    // When city changes: adjust selection + animate camera
    LaunchedEffect(selectedCity) {
        val targetEvent = filteredEvents.firstOrNull()
        selectedEvent = targetEvent

        val (targetLatLng, zoom) =
            if (selectedCity == "All South Africa" || targetEvent == null) {
                LatLng(-29.0, 24.0) to 5.2f
            } else {
                targetEvent.position to 10f
            }

        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(targetLatLng, zoom),
            durationMs = 650
        )
    }

    // When a specific event is selected: zoom in slightly on it
    LaunchedEffect(selectedEvent) {
        selectedEvent?.let { ev ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(ev.position, 11f),
                durationMs = 500
            )
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF02030A)),
        color = Color(0xFF02030A)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // TOP BAR (title + Artists nav)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Simple text back action (optional)
                Text(
                    text = "LeVibe • Explore events",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onBack() }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Artists",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { onArtistsClick() }
                    )
                }
            }

            // MAP SECTION
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        compassEnabled = false,
                        mapToolbarEnabled = false
                    ),
                    properties = MapProperties(
                        isMyLocationEnabled = false
                    )
                ) {
                    filteredEvents.forEach { event ->
                        Marker(
                            state = MarkerState(position = event.position),
                            title = event.title,
                            snippet = "${event.venue} • ${event.city}",
                            onClick = {
                                selectedEvent = event
                                true
                            },
                            icon = BitmapDescriptorFactory.defaultMarker(
                                if (selectedEvent?.id == event.id) 204f // highlight
                                else 0f
                            )
                        )
                    }
                }

                // City selector chip + dropdown
                CityFilterChip(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    cityOptions = cityOptions,
                    selectedCity = selectedCity,
                    onCitySelected = { selectedCity = it }
                )
            }

            // BOTTOM CONSOLE
            BottomEventConsole(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f),
                selectedEvent = selectedEvent,
                hasEvents = filteredEvents.isNotEmpty()
            )
        }
    }
}

/**
 * "All South Africa" chip with dropdown city list.
 */
@Composable
private fun CityFilterChip(
    modifier: Modifier = Modifier,
    cityOptions: List<String>,
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xCC111827),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
                )
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = selectedCity,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFF020814)
        ) {
            cityOptions.forEach { city ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = city,
                            color = if (city == selectedCity) Color(0xFF38BDF8) else Color.White,
                            fontSize = 13.sp
                        )
                    },
                    onClick = {
                        expanded = false
                        onCitySelected(city)
                    }
                )
            }
        }
    }
}

/**
 * Bottom glass-style console: hint + selected event + Rides/Tickets/Safety tabs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomEventConsole(
    modifier: Modifier = Modifier,
    selectedEvent: MapEvent?,
    hasEvents: Boolean
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Rides", "Tickets", "Safety")

    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xF0101117),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(
                        topStart = 26.dp,
                        topEnd = 26.dp
                    )
                )
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (hasEvents)
                        "Tap any pin on the map to preview the event and plan your night."
                    else
                        "No events in this city yet. Try another city or All South Africa.",
                    color = Color(0xFF9CA3AF),
                    fontSize = 12.sp
                )

                AnimatedVisibility(
                    visible = selectedEvent != null && hasEvents,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    selectedEvent?.let { event ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF020814)
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = event.title,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${event.venue} • ${event.city}",
                                    color = Color(0xFF9CA3AF),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "${event.dateLabel} • ${event.priceFrom}",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Tabs
                if (hasEvents) {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabs.forEachIndexed { index, label ->
                            SegmentedButton(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = tabs.size
                                ),
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = Color.White,
                                    activeContentColor = Color(0xFF111827),
                                    inactiveContainerColor = Color(0x00111827),
                                    inactiveContentColor = Color(0xFFE5E5E5)
                                )
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> RidesContent()
                        1 -> TicketsContent()
                        2 -> SafetyContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun RidesContent() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Suggested rides (mock data for MVP):",
            color = Color(0xFF9CA3AF),
            fontSize = 11.sp
        )
        RideRow("Get Uber (Go now)", "~11 min • est. R90")
        RideRow("Get Uber (After party)", "~11 min • est. R120")
        RideRow("Bolt", "~13 min • est. R80")
    }
}

@Composable
private fun RideRow(label: String, meta: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF020814),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = meta,
                color = Color(0xFF9CA3AF),
                fontSize = 10.sp
            )
        }
        Button(
            onClick = { /* hook up later */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF111827)
            ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(999.dp)
        ) {
            Text("Open", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun TicketsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Tickets (placeholder UI):",
            color = Color(0xFF9CA3AF),
            fontSize = 11.sp
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF020814)
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "From R120 • Limited tickets left",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Wire this up to your real ticketing flow later.",
                    color = Color(0xFF9CA3AF),
                    fontSize = 10.sp
                )
                Spacer(Modifier.height(4.dp))
                Button(
                    onClick = { /* ticket link */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF111827)
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(999.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
                ) {
                    Text("Buy Ticket", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SafetyContent() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Safety & transit tips:",
            color = Color(0xFF9CA3AF),
            fontSize = 11.sp
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF020814)
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "• Prefer e-hailing or trusted transport when leaving late.",
                    color = Color.White,
                    fontSize = 11.sp
                )
                Text(
                    text = "• Share your trip with friends.",
                    color = Color.White,
                    fontSize = 11.sp
                )
                Text(
                    text = "• Meet at well-lit pickup zones & stay aware of surroundings.",
                    color = Color.White,
                    fontSize = 11.sp
                )
            }
        }
    }
}
