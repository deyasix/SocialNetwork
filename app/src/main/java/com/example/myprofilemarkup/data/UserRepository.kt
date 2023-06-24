package com.example.myprofilemarkup.data

object UserRepository {
    private const val photo =
        "https://www.akc.org/wp-content/uploads/2021/07/Cavalier-King-Charles-Spaniel-laying-down-indoors.jpeg"
    val users = mutableListOf(
        User(1, photo, "Ava Smith", "Photograph", ""),
        User(2, photo, "Jessie Brown", "Actress", ""),
        User(3, photo, "Jackie Taylor", "Financier", ""),
        User(4, photo, "Jenny Walker", "Make-up artist", ""),
        User(5, photo, "Freddy Harris", "Secretary", ""),
        User(6, photo, "Annie King", "Nurse", ""),
    )
}