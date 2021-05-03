package by.sitko.speedroommating.domain

import java.util.*

data class EventDomainItem(
    val cost: String,
    val endTime: Date,
    val imageUrl: String,
    val location: String,
    val phoneNumber: String,
    val startTime: Date,
    val venue: String
)