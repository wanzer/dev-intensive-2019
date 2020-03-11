package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, timeUnit: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (timeUnit) {
        //"second", "seconds" -> value * SECOND
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
        else -> throw IllegalStateException("invalid unit")
    }
    this.time = time
    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.humanizeDiff(date: Date = Date()): String {

    val isInPast = date.time >= this.time
    return when (val diff = (Math.abs(date.time - this.time))) {
        in TimeUnit.SECOND.toMillis(0)..TimeUnit.SECOND.toMillis(1) -> "только что"
        in TimeUnit.SECOND.toMillis(0)..TimeUnit.SECOND.toMillis(45) -> if (isInPast) "несколько секунд назад" else "через несколько секунд"
        in TimeUnit.SECOND.toMillis(45)..TimeUnit.SECOND.toMillis(75) -> if (isInPast) "минуту назад" else "через минуту"
        in TimeUnit.SECOND.toMillis(75)..TimeUnit.MINUTE.toMillis(45) -> TimeUnit.MINUTE.getDeclentedRepresentation(
            diff,
            isInPast
        )
        in TimeUnit.MINUTE.toMillis(45)..TimeUnit.MINUTE.toMillis(75) -> if (isInPast) "час назад" else "через час"
        in TimeUnit.MINUTE.toMillis(75)..TimeUnit.HOUR.toMillis(22) -> TimeUnit.HOUR.getDeclentedRepresentation(
            diff,
            isInPast
        )
        in TimeUnit.HOUR.toMillis(22)..TimeUnit.HOUR.toMillis(26) -> if (isInPast) "день назад" else "через день"
        in TimeUnit.HOUR.toMillis(26)..TimeUnit.DAY.toMillis(360) -> TimeUnit.DAY.getDeclentedRepresentation(
            diff,
            isInPast
        )
        else -> if (isInPast) "более года назад" else "более чем через год"
    }
}

enum class TimeUnit(
    private val convertValue: Long,
    private val declentionValues: List<String>
) {
    SECOND(1000, listOf("секунду", "секунды", "секунд")),
    MINUTE(1000 * 60, listOf("минуту", "минуты", "минут")),
    HOUR(1000 * 60 * 60, listOf("час", "часа", "часов")),
    DAY(1000 * 60 * 60 * 24, listOf("день", "дня", "дней"));

    fun toMillis(value: Int) = convertValue * value
    fun getValueFromMillis(value: Long) = value / convertValue
    fun getDeclentedRepresentation(value: Long, isInPast: Boolean) =
        "${if (isInPast) "" else "через "}${getValueFromMillis(value)} ${getDeclentedStringValue(
            getValueFromMillis(value)
        )}${if (isInPast) " назад" else ""}"

    private fun getDeclentedStringValue(value: Long): String {
        return when (val resultValue = Math.abs(value) % 100) {
            1L -> declentionValues[0]
            in 2..4 -> declentionValues[1]
            0L, in 5..20 -> declentionValues[2]
            else -> getDeclentedStringValue(resultValue % 10)
        }
    }
}