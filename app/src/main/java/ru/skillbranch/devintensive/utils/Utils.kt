package ru.skillbranch.devintensive.utils

import java.util.*

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {

        return if(fullName.isNullOrBlank()){
            null to null
        }else {
            val parts: List<String>? = fullName.split(" ")
            val firstName = parts?.getOrNull(0)
            val lastName = parts?.getOrNull(1)
            firstName to lastName
        }
    }

    fun toInitials(firstName: String?, lastName: String?): String? {

        if (firstName.isNullOrBlank() && lastName.isNullOrBlank()){
            return null
        }
        val first = firstName?.trim()?.toUpperCase(Locale.getDefault())?.getOrNull(0)
        val last = lastName?.trim()?.toUpperCase(Locale.getDefault())?.getOrNull(0)

        return "${first?: ""} ${last?: ""}"
    }

    fun transliteration(payload: String, divider: String? = " "): String {
        return payload.map {
            when(it) {
                'а' -> "a"
                'б' -> "b"
                'в' -> "v"
                'г' -> "g"
                'д' -> "d"
                'е' -> "e"
                'ё' -> "e"
                'ж' -> "zh"
                'з' -> "z"
                'и' -> "i"
                'й' -> "i"
                'к' -> "k"
                'л' -> "l"
                'м' -> "m"
                'н' -> "n"
                'о' -> "o"
                'п' -> "p"
                'р' -> "r"
                'с' -> "s"
                'т' -> "t"
                'у' -> "u"
                'ф' -> "f"
                'х' -> "h"
                'ц' -> "c"
                'ч' -> "ch"
                'ш' -> "sh"
                'щ' -> "sh'"
                'ъ' -> ""
                'ы' -> "i"
                'ь' -> ""
                'э' -> "e"
                'ю' -> "yu"
                'я' -> "ya"
                'А' -> "A"
                'Б' -> "B"
                'В' -> "V"
                'Г' -> "G"
                'Д' -> "D"
                'Е' -> "E"
                'Ё' -> "E"
                'Ж' -> "Zh"
                'З' -> "Z"
                'И' -> "I"
                'Й' -> "I"
                'К' -> "K"
                'Л' -> "L"
                'М' -> "M"
                'Н' -> "N"
                'О' -> "O"
                'П' -> "P"
                'Р' -> "R"
                'С' -> "S"
                'Т' -> "T"
                'У' -> "U"
                'Ф' -> "F"
                'Х' -> "H"
                'Ц' -> "C"
                'Ч' -> "Ch"
                'Ш' -> "Sh"
                'Щ' -> "Sh'"
                'Ъ' -> ""
                'Ы' -> "I"
                'Ь' -> ""
                'Э' -> "E"
                'Ю' -> "Yu"
                'Я' -> "Ya"
                else ->it.toString()
            }
        }.joinToString("").replace(" ", divider.toString())
    }
}