package com.example.levibegg.ui.screens

import com.example.levibegg.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levibegg.ui.components.GlassButton
import com.example.levibegg.data.Ticket
import com.example.levibegg.data.TicketRepository
import java.util.UUID

// --- MINI MAP FOR EVENT LOCATION --------------------------------------------

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// --- CALENDAR IMPORTS -------------------------------------------------------

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/* -------------------------------------------------------------------------- */
/*  UI MODEL                                                                  */
/* -------------------------------------------------------------------------- */

data class UiEvent(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val dateLabel: String,
    val priceLabel: String,
    val location: String,
    val description: String,
    val imageResId: Int,
    val latitude: Double,
    val longitude: Double
)

/*  Helper: parse "DEC 21" into a LocalDate for the given year                */

private fun UiEvent.toLocalDate(year: Int): LocalDate? {
    val parts = dateLabel.trim().split(" ")
    if (parts.size != 2) return null

    val monthStr = parts[0].uppercase(Locale.getDefault())
    val day = parts[1].toIntOrNull() ?: return null

    val month = when (monthStr) {
        "JAN" -> 1
        "FEB" -> 2
        "MAR" -> 3
        "APR" -> 4
        "MAY" -> 5
        "JUN" -> 6
        "JUL" -> 7
        "AUG" -> 8
        "SEP" -> 9
        "OCT" -> 10
        "NOV" -> 11
        "DEC" -> 12
        else -> return null
    }

    return try {
        LocalDate.of(year, month, day)
    } catch (e: Exception) {
        null
    }
}

/* -------------------------------------------------------------------------- */
/*  EXPLORE EVENTS – MAIN USER PAGE + INLINE DETAILS + TICKETS + PROFILE      */
/* -------------------------------------------------------------------------- */

@Composable
fun ExploreEventsScreen(
    onProfileClick: () -> Unit = {}
) {
    var selectedEvent by remember { mutableStateOf<UiEvent?>(null) }

    // When not null → show checkout form for that event
    var checkoutForEvent by remember { mutableStateOf<UiEvent?>(null) }

    // Bottom nav index: 0 = Explore, 1 = Tickets, 2 = Calendar, 3 = Profile
    var activeTab by remember { mutableStateOf(0) }

    val featuredEvents = remember {
        listOf(
            UiEvent(
                id = "1",
                title = "The Weekend",
                subtitle = "CONCERT",
                category = "Concert",
                dateLabel = "DEC 21",
                priceLabel = "R350 / ticket",
                location = "Johannesburg • Inner city arena",
                description = "Turn the city into a playground of lights, music and rhythm with a full Weekend headline set, surprise guests and an after-party till sunrise.",
                imageResId = R.drawable.weekend_poster,
                latitude = -26.2041,
                longitude = 28.0473
            ),
            UiEvent(
                id = "2",
                title = "Midnight Drive",
                subtitle = "CAR MEET",
                category = "Car meet",
                dateLabel = "DEC 27",
                priceLabel = "R150 / entry",
                location = "Pretoria • Rooftop parkade",
                description = "Late-night car culture with neon lights, live DJs and food trucks. Show & shine, sound-offs and a rooftop sunset view.",
                imageResId = R.drawable.car_meet_poster,
                latitude = -25.7479,
                longitude = 28.2293
            ),
            UiEvent(
                id = "3",
                title = "Firemasters",
                subtitle = "SHOW",
                category = "Show",
                dateLabel = "DEC 21",
                priceLabel = "R280 / ticket",
                location = "Johannesburg • Fire arena",
                description = "Explosive night event packed with live DJs, fire shows and high energy dancers. Afro-house, amapiano and everything in between.",
                imageResId = R.drawable.festival_poster,
                latitude = -26.2041,
                longitude = 28.0473
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF02030A)
    ) {
        val event = selectedEvent

        // 1️⃣ Highest priority: checkout form
        if (checkoutForEvent != null) {
            TicketCheckoutScreen(
                event = checkoutForEvent!!,
                onBack = { checkoutForEvent = null },
                onTicketConfirmed = { ticket ->
                    TicketRepository.addTicket(ticket)
                    checkoutForEvent = null
                    selectedEvent = null
                    activeTab = 1 // jump to Tickets tab
                }
            )
        }
        // 2️⃣ Event details page
        else if (event != null) {
            ExploreEventDetailsContent(
                event = event,
                onBack = { selectedEvent = null },
                onBuyTicket = { checkoutForEvent = event }
            )
        }
        // 3️⃣ Normal explore / tickets / calendar / profile with bottom nav
        else {
            Box(modifier = Modifier.fillMaxSize()) {

                when (activeTab) {
                    0 -> {
                        ExploreEventsListContent(
                            featuredEvents = featuredEvents,
                            onProfileClick = onProfileClick,
                            onEventClick = { tapped -> selectedEvent = tapped },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 84.dp)
                        )
                    }

                    1 -> {
                        TicketsScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 84.dp)
                        )
                    }

                    2 -> {
                        EventsCalendarScreen(
                            events = featuredEvents,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 84.dp)
                        )
                    }

                    3 -> {
                        ProfileScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 84.dp)
                        )
                    }
                }

                BottomNavBar(
                    selectedIndex = activeTab,
                    onItemSelected = { index -> activeTab = index },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 18.dp, vertical = 18.dp)
                )
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  LIST PAGE (TOP SECTION + FEATURED + FOR YOU)                              */
/* -------------------------------------------------------------------------- */

@Composable
private fun ExploreEventsListContent(
    featuredEvents: List<UiEvent>,
    onProfileClick: () -> Unit,
    onEventClick: (UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .verticalScroll(scrollState)
    ) {

        // Top: date + title + profile bubble
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DECEMBER 16, 9:10 PM",
                    color = Color(0xFF9CA3AF),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Explore events",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF111827))
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "K",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Search + filter row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF111827))
                    .clickable { /* TODO: filters bottom sheet */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // FEATURED
        Text(
            text = "FEATURED",
            color = Color(0xFF9CA3AF),
            fontSize = 11.sp,
            letterSpacing = 0.15.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(featuredEvents) { event ->
                FeaturedEventCard(
                    event = event,
                    onClick = { onEventClick(event) }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // FOR YOU
        Text(
            text = "FOR YOU",
            color = Color(0xFF9CA3AF),
            fontSize = 11.sp,
            letterSpacing = 0.15.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        ForYouCard()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/* -------------------------------------------------------------------------- */
/*  SMALL BUILDING BLOCKS                                                     */
/* -------------------------------------------------------------------------- */

@Composable
private fun SearchBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF111827))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color(0xFF6B7280)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Search",
            color = Color(0xFF6B7280),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun FeaturedEventCard(
    event: UiEvent,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(260.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF02030A))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(26.dp))
        ) {
            Image(
                painter = painterResource(id = event.imageResId),
                contentDescription = event.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC000000)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF97316))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = event.dateLabel,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x99000000))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = event.subtitle,
                    color = Color.White,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(horizontal = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = event.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = event.location,
                color = Color(0xFF9CA3AF),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun ForYouCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF6366F1),
                        Color(0xFFEC4899),
                        Color(0xFFF97316)
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Claim 1 free ticket!",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Share an event with friends and get 1 ticket.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
            )
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  BOTTOM NAV                                                                */
/* -------------------------------------------------------------------------- */

@Composable
private fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFF02030A))
            .border(
                width = 1.dp,
                color = Color(0x331F2937),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Default.Home,
            label = "Featured",
            isSelected = selectedIndex == 0,
            onClick = { onItemSelected(0) }
        )
        BottomNavItem(
            icon = Icons.Default.ConfirmationNumber,
            label = "Tickets",
            isSelected = selectedIndex == 1,
            onClick = { onItemSelected(1) }
        )
        BottomNavItem(
            icon = Icons.Default.Event,
            label = "Calendar",
            isSelected = selectedIndex == 2,
            onClick = { onItemSelected(2) }
        )
        BottomNavItem(
            icon = Icons.Default.Person,
            label = "Profile",
            isSelected = selectedIndex == 3,
            onClick = { onItemSelected(3) }
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(
                    if (isSelected) Color(0xFF111827) else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White
            )
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  DETAILS VIEW                                                              */
/* -------------------------------------------------------------------------- */

@Composable
private fun ExploreEventDetailsContent(
    event: UiEvent,
    onBack: () -> Unit,
    onBuyTicket: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // HERO IMAGE WITH OVERLAYED TITLE TEXT
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {

            Image(
                painter = painterResource(id = event.imageResId),
                contentDescription = event.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC000000)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconBubble(
                    icon = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    onClick = onBack
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    IconBubble(
                        icon = Icons.Default.FavoriteBorder,
                        contentDescription = "Like"
                    )
                    IconBubble(
                        icon = Icons.Default.Share,
                        contentDescription = "Share"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF97316))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = event.dateLabel,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 18.dp, bottom = 24.dp)
            ) {
                Text(
                    text = event.subtitle.uppercase(),
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp
                )
                Text(
                    text = event.title,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "STARTING 9:10 PM",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFF02030A))
                    .border(
                        1.dp,
                        Color(0x331F2937),
                        RoundedCornerShape(999.dp)
                    )
                    .padding(4.dp)
            ) {
                TabPill(
                    label = "ABOUT",
                    isSelected = true,
                    modifier = Modifier.weight(1f)
                )
                TabPill(
                    label = "PARTICIPANTS",
                    isSelected = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = event.description,
                color = Color(0xFF9CA3AF),
                fontSize = 13.sp
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "LOCATION",
                    color = Color(0xFF9CA3AF),
                    fontSize = 11.sp
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF020814))
                        .border(
                            1.dp,
                            Color(0x331F2937),
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    MiniEventMap(
                        event = event,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp
                                )
                            )
                    )
                    Text(
                        text = event.location,
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PRICE",
                        color = Color(0xFF9CA3AF),
                        fontSize = 11.sp
                    )
                    Text(
                        text = event.priceLabel,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                GlassButton(
                    text = "BUY A TICKET",
                    isPrimary = true
                ) {
                    onBuyTicket()
                }
            }
        }
    }
}

@Composable
private fun IconBubble(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0x66000000))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

@Composable
private fun TabPill(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (isSelected) Color.White else Color.Transparent
            )
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else Color(0xFF9CA3AF),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  TICKET CHECKOUT + TICKETS TAB                                             */
/* -------------------------------------------------------------------------- */

@Composable
private fun TicketCheckoutScreen(
    event: UiEvent,
    onBack: () -> Unit,
    onTicketConfirmed: (Ticket) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("1") }

    val qty = quantityText.toIntOrNull() ?: 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF02030A))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBubble(
                icon = Icons.Default.ArrowBack,
                contentDescription = "Back",
                onClick = onBack
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Checkout",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Event summary
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF050815))
                .padding(16.dp)
        ) {
            Text(
                text = event.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${event.dateLabel} • ${event.location}",
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = event.priceLabel,
                color = Color(0xFF22C55E),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = quantityText,
            onValueChange = { quantityText = it.filter { ch -> ch.isDigit() } },
            label = { Text("Number of tickets") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = {
                val ticket = Ticket(
                    id = UUID.randomUUID().toString(),
                    eventId = event.id,
                    eventTitle = event.title,
                    eventDateLabel = event.dateLabel,
                    location = event.location,
                    buyerName = name.ifBlank { "Guest" },
                    buyerEmail = email.ifBlank { "unknown@levibe.app" },
                    quantity = qty,
                    priceLabel = event.priceLabel,
                    purchasedAt = System.currentTimeMillis()
                )
                onTicketConfirmed(ticket)
            }
        ) {
            Text("Confirm purchase", fontSize = 16.sp)
        }
    }
}

@Composable
private fun MiniEventMap(
    event: UiEvent,
    modifier: Modifier = Modifier
) {
    val eventLatLng = LatLng(event.latitude, event.longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(eventLatLng, 12f)
    }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        ),
        properties = MapProperties()
    ) {
        Marker(
            state = MarkerState(position = eventLatLng),
            title = event.title,
            snippet = event.location
        )
    }
}

@Composable
private fun TicketsScreen(
    modifier: Modifier = Modifier
) {
    val tickets = TicketRepository.tickets

    Column(
        modifier = modifier
            .background(Color(0xFF02030A))
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Text(
            text = "Your tickets",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (tickets.isEmpty()) {
            Text(
                text = "You don’t have any tickets yet.\nTap an event and buy a ticket to see it here.",
                color = Color(0xFF9CA3AF),
                fontSize = 13.sp
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tickets) { ticket ->
                    TicketCard(ticket = ticket)
                }
            }
        }
    }
}

@Composable
private fun TicketCard(ticket: Ticket) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF050815))
            .border(
                1.dp,
                Color(0x331F2937),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = ticket.eventTitle,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "${ticket.eventDateLabel} • ${ticket.location}",
            color = Color(0xFF9CA3AF),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Name: ${ticket.buyerName}",
            color = Color(0xFFCBD5E1),
            fontSize = 12.sp
        )
        Text(
            text = "x${ticket.quantity} • ${ticket.priceLabel}",
            color = Color(0xFFCBD5E1),
            fontSize = 12.sp
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  PROFILE SCREEN + SETTINGS                                                 */
/* -------------------------------------------------------------------------- */

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val tickets: List<Ticket> = TicketRepository.tickets

    val defaultName = "Your name"
    val defaultEmail = "you@example.com"

    val name = tickets.firstOrNull()?.buyerName ?: defaultName
    val email = tickets.firstOrNull()?.buyerEmail ?: defaultEmail

    val initials = name
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.first().uppercaseChar() }
        .take(2)
        .joinToString("")

    val totalTickets = tickets.sumOf { it.quantity }
    val totalEvents = tickets.distinctBy { it.eventTitle }.size

    Column(
        modifier = modifier
            .background(Color(0xFF02030A))
            .padding(horizontal = 18.dp, vertical = 18.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header: avatar + name + edit chip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF6366F1),
                                    Color(0xFF02030A)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = email,
                        color = Color(0xFF9CA3AF),
                        fontSize = 12.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFF111827))
                    .clickable { /* TODO: open edit profile later */ }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit profile",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Edit",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard(
                label = "Tickets",
                value = totalTickets.toString(),
                modifier = Modifier.weight(1f)
            )
            ProfileStatCard(
                label = "Events",
                value = totalEvents.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Your events / past tickets
        Text(
            text = "Your events",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (tickets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF020814))
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "You haven’t bought any tickets yet.\nGrab a seat at your next event!",
                    color = Color(0xFF9CA3AF),
                    fontSize = 12.sp
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                tickets.forEach { ticket ->
                    TicketCard(ticket = ticket)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Settings section
        Text(
            text = "Settings",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileSettingRow(
            icon = Icons.Default.Notifications,
            label = "Notifications"
        )

        ProfileSettingRow(
            icon = Icons.Default.CreditCard,
            label = "Payment methods"
        )

        ProfileSettingRow(
            icon = Icons.Default.HelpOutline,
            label = "Help & support"
        )

        ProfileSettingRow(
            icon = Icons.Default.Settings,
            label = "App settings"
        )

        ProfileSettingRow(
            icon = Icons.Default.Logout,
            label = "Log out"
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun ProfileStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF111827),
                        Color(0xFF02030A)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color(0x331F2937),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color(0xFF9CA3AF),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ProfileSettingRow(
    icon: ImageVector,
    label: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF020814))
            .clickable { /* TODO: hook up later */ }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF111827)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  CALENDAR SCREEN WITH EVENTS                                               */
/* -------------------------------------------------------------------------- */

@Composable
private fun EventsCalendarScreen(
    events: List<UiEvent>,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.from(today)) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(today) }

    val monthTitle = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val year = currentMonth.year

    // Map of LocalDate -> events on that date
    val eventsByDate = remember(events, currentMonth) {
        events.mapNotNull { event ->
            val date = event.toLocalDate(currentMonth.year)
            if (date != null) date to event else null
        }.groupBy({ it.first }, { it.second })
    }

    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")

    val firstOfMonth = currentMonth.atDay(1)
    // DayOfWeek.value: Mon=1..Sun=7. We want Sunday = 0
    val leadingEmpty = (firstOfMonth.dayOfWeek.value % 7)
    val daysInMonth = currentMonth.lengthOfMonth()
    val totalCells = leadingEmpty + daysInMonth
    val rows = (totalCells + 6) / 7

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .background(Color(0xFF02030A))
            .padding(horizontal = 18.dp, vertical = 18.dp)
            .verticalScroll(scrollState)
    ) {
        // Header row: month + year + mini nav
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Calendar",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$monthTitle $year",
                    color = Color(0xFF9CA3AF),
                    fontSize = 13.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFF111827))
                        .clickable {
                            currentMonth = currentMonth.minusMonths(1)
                            selectedDate = currentMonth.atDay(1)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "<",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFF111827))
                        .clickable {
                            currentMonth = currentMonth.plusMonths(1)
                            selectedDate = currentMonth.atDay(1)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ">",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Calendar container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF020814))
                .border(
                    width = 1.dp,
                    color = Color(0x331F2937),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 14.dp, vertical = 14.dp)
        ) {
            // Days of week header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfWeek.forEach { label ->
                    Text(
                        text = label,
                        color = Color(0xFF6B7280),
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grid of days
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNumber =
                            if (cellIndex >= leadingEmpty && cellIndex < leadingEmpty + daysInMonth) {
                                cellIndex - leadingEmpty + 1
                            } else null

                        if (dayNumber == null) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        } else {
                            val date = currentMonth.atDay(dayNumber)
                            val hasEvent = eventsByDate.containsKey(date)
                            val isSelected = selectedDate == date

                            val bgBrush = when {
                                isSelected && hasEvent -> Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF6366F1),
                                        Color(0xFFEC4899)
                                    )
                                )
                                isSelected -> Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF111827),
                                        Color(0xFF02030A)
                                    )
                                )
                                else -> null
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        brush = bgBrush ?: Brush.verticalGradient(
                                            listOf(
                                                if (hasEvent) Color(0xFF020814) else Color.Transparent,
                                                if (hasEvent) Color(0xFF020814) else Color.Transparent
                                            )
                                        )
                                    )
                                    .clickable { selectedDate = date },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = dayNumber.toString(),
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                    )
                                    if (hasEvent) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFF97316))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Selected day events
        val selectedEvents = selectedDate?.let { eventsByDate[it] }.orEmpty()

        selectedDate?.let { date ->
            val dayLabel =
                "${date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())} " +
                        "${date.dayOfMonth} " +
                        date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

            Text(
                text = dayLabel,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (selectedEvents.isEmpty()) {
                Text(
                    text = "No events on this day.",
                    color = Color(0xFF9CA3AF),
                    fontSize = 13.sp
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    selectedEvents.forEach { event ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF050815))
                                .border(
                                    1.dp,
                                    Color(0x331F2937),
                                    RoundedCornerShape(18.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = event.title,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = event.location,
                                color = Color(0xFF9CA3AF),
                                fontSize = 12.sp
                            )
                            Text(
                                text = event.priceLabel,
                                color = Color(0xFF22C55E),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
