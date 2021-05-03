package by.sitko.speedroommating.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

val yyyy_mm_dd_hh_mm_ss_z = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK)
val dd_MMM = SimpleDateFormat("dd MMM", Locale.UK)
val hh_mm = SimpleDateFormat("hh:mm", Locale.UK)
val hh_mm_aa = SimpleDateFormat("hh:mm aa", Locale.UK)

fun Date.isPastDay(): Boolean {
    val today = Calendar.getInstance()

    val comparingDateCalendar = Calendar.getInstance().clone() as Calendar
    comparingDateCalendar.time = this

    return comparingDateCalendar.get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR)
            || comparingDateCalendar.get(Calendar.YEAR) < today.get(Calendar.YEAR)
}
