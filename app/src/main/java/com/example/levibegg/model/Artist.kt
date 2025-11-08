package com.example.levibegg.data.model

data class ArtistSocials(
    val x: String? = null,
    val ig: String? = null
)

data class Artist(
    val id: String,
    val name: String,
    val verified: Boolean,
    val avatar: String,
    val banner: String,
    val genre: List<String>,
    val bio: String,
    val socials: ArtistSocials = ArtistSocials()
)
