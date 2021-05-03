package by.sitko.speedroommating.data

import by.sitko.speedroommating.Mapper
import by.sitko.speedroommating.domain.EventDomainItem
import by.sitko.speedroommating.presentation.utils.yyyy_mm_dd_hh_mm_ss_z

object DataToDomainEventMapper : Mapper<EventItem, EventDomainItem>() {

    override fun map(value: EventItem) = EventDomainItem(
        value.cost!!,
        yyyy_mm_dd_hh_mm_ss_z.parse(value.end_time!!)!!,
        value.image_url!!,
        value.location!!,
        value.phone_number!!,
        yyyy_mm_dd_hh_mm_ss_z.parse(value.start_time!!)!!,
        value.venue!!
    )
}