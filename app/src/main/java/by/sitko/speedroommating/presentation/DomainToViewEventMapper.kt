package by.sitko.speedroommating.presentation

import by.sitko.speedroommating.Mapper
import by.sitko.speedroommating.domain.EventDomainItem
import by.sitko.speedroommating.presentation.utils.dd_MMM
import by.sitko.speedroommating.presentation.utils.hh_mm
import by.sitko.speedroommating.presentation.utils.hh_mm_aa
import by.sitko.speedroommating.presentation.utils.yyyy_mm_dd_hh_mm_ss_z
import java.util.*

object DomainToViewEventMapper : Mapper<EventDomainItem, EventViewItem>() {

    override fun map(value: EventDomainItem) = EventViewItem(
        value.cost.toUpperCase(),
        formEventDate(value.startTime),
        formEventDuration(value.startTime, value.endTime),
        value.location,
        value.imageUrl,
        value.venue
    )

    private fun formEventDuration(startTime: Date, endTime: Date): String {
        return "${hh_mm.format(startTime)} - ${hh_mm_aa.format(endTime)}"
    }

    private fun formEventDate(startDate: Date): String {
        return dd_MMM.format(startDate)
    }
}