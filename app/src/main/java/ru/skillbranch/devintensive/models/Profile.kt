package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils

class Profile(
    val rank: String = "Junior android developer",
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {
    private var nickName: String = Utils.transliteration("$firstName $lastName", "_")

    fun toMap(): Map<String, Any> = mapOf(
        "nickName" to nickName,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )
}