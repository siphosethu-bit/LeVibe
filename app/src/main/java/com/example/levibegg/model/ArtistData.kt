package com.example.levibegg.data.model

/**
 * Static seed data mirroring your site JSON.
 * Used by ArtistsScreen.
 */
object ArtistData {

    val artists: List<Artist> = listOf(
        Artist(
            id = "tyla",
            name = "Tyla",
            verified = true,
            avatar = "https://i.pravatar.cc/120?img=65",
            banner = "https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=2000&auto=format&fit=crop",
            genre = listOf("Pop", "Amapiano"),
            bio = "Grammy-winning SA pop/R&B star known for viral hits and high-energy sets.",
            socials = ArtistSocials(
                x = "https://x.com/tyla",
                ig = "https://instagram.com/tyla"
            )
        ),
        Artist(
            id = "kabza",
            name = "Kabza De Small",
            verified = true,
            avatar = "https://i.pravatar.cc/120?img=12",
            banner = "https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?q=80&w=2000&auto=format&fit=crop",
            genre = listOf("Amapiano"),
            bio = "The 'King of Amapiano' — prolific producer/DJ with marathon live sets.",
            socials = ArtistSocials(
                x = "https://x.com/kabzadesmall_",
                ig = "https://instagram.com/kabza_de_small"
            )
        ),
        // paste the rest of your JSON-mapped Artist(...) entries here
    )
}
