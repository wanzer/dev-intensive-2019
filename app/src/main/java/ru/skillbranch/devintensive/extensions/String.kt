package ru.skillbranch.devintensive.extensions

fun String.truncate(charactersCount: Int = 16): String {
    val strLength = this.trim().length
    return if (strLength > 0 && strLength > charactersCount)
        this.trim().substring(0, charactersCount).trim().plus("...")
    else
        this.trim()
}

fun String.stripHtml() = this.replace(Regex("<[^<]*?>|&\\d+;"), "")
    .replace(Regex("\\s+"), " ")