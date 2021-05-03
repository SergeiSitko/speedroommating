package by.sitko.speedroommating.data

import by.sitko.speedroommating.domain.EventDomainItem
import by.sitko.speedroommating.domain.LoadEventsUseCase
import by.sitko.speedroommating.domain.Repository
import by.sitko.speedroommating.presentation.utils.isPastDay

class LoadEventsUseCaseImpl(
    private val repository: Repository
) : LoadEventsUseCase {

    override suspend fun loadUpcomingEvents(): List<EventDomainItem> {
        val allEvents = repository.loadData()
        if (allEvents.isEmpty()) return emptyList()

        return allEvents.filterNot { it.startTime.isPastDay() }.sortedBy { it.startTime }
    }

    override suspend fun loadPastEvents(): List<EventDomainItem> {
        val allEvents = repository.loadData()
        if (allEvents.isEmpty()) return emptyList()

        return allEvents.filter { it.startTime.isPastDay() }.sortedByDescending { it.startTime }
    }
}